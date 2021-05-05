<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
    </head>
    <body>

    <div class="layui-fluid">
        <div class="layui-card">
            <div class="layui-form layui-card-header layuiadmin-card-header-auto">
                <div class="layui-form-item">
                    <div class="layui-inline">路由描述</div>
                    <div class="layui-inline" style="width:500px">
                        <input type="text" name="description" placeholder="请输入路由描述" autocomplete="off"
                               class="layui-input">
                    </div>
                    <div class="layui-inline">
                        <button id="search" class="layui-btn layuiadmin-btn-admin" lay-submit lay-filter="search">
                            <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                        </button>
                    </div>
                </div>
            </div>

            <div class="layui-card-body">
                <table id="grid" lay-filter="grid"></table>

                <script type="text/html" id="grid-toolbar">
                    <div class="layui-btn-container">
                        <@insert>
                            <button class="layui-btn layui-btn-sm layuiadmin-btn-admin"
                                    lay-event="add"><i
                                        class="layui-icon layui-icon-add-1"></i>&nbsp;&nbsp;新增路由
                            </button>
                        </@insert>
                        <@delete>
                            <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">
                                <i class="layui-icon layui-icon-delete"></i>&nbsp;&nbsp;删除路由
                            </button>
                        </@delete>
                        <@select>
                            <button class="layui-btn layui-btn-sm layui-btn-primary layuiadmin-btn-admin"
                                    lay-event="working"><i
                                        class="layui-icon layui-icon-read"></i>&nbsp;&nbsp;查看正在工作路由
                            </button>
                        </@select>
                        <@update>
                            <button id="btnPublish"
                                    class="layui-btn-disabled layui-btn layui-btn-sm layui-btn-normal layuiadmin-btn-admin"
                                    lay-event="publish" style="margin-left: 50px">
                                <i class="layui-icon layui-icon-release"></i>&nbsp;&nbsp;发布路由
                                <span id="spanStatus" class="layui-badge layui-bg-orange"
                                      style="display: none">有修改</span>
                            </button>
                        </@update>
                    </div>
                </script>

                <script type="text/html" id="grid-bar">
                    <@update>
                        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i
                                    class="layui-icon layui-icon-edit"></i>编辑</a>

                        {{#  if(d.enabled){ }}
                        <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="disable">
                            <i class="layui-icon layui-icon-logout"></i>禁用</a>
                        {{#  } else { }}
                        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="enable">
                            <i class="layui-icon layui-icon-ok"></i>启用</a>
                        {{#  } }}
                    </@update>
                </script>
            </div>
        </div>
    </div>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
            const admin = layui.admin, $ = layui.$, form = layui.form, table = layui.table;
            let isUpdate = false;
            tableErrorHandler();
            form.on('submit(search)', function (data) {
                const field = data.field;
                table.reload('grid', {page: {curr: 1}, where: field});
                updateStatus(false);
            });
            table.render({
                elem: '#grid',
                url: 'list',
                toolbar: '#grid-toolbar',
                method: 'post',
                cellMinWidth: 80,
                page: true,
                limit: 15,
                limits: [15],
                even: true,
                text: {
                    none: '暂无相关数据'
                },
                cols: [[
                    {type: 'checkbox'},
                    {type: 'numbers', title: '序号', width: 50},
                    {field: 'description', title: '路由描述', width: 300},
                    {field: 'uri', title: '目标地址', width: 300},
                    {field: 'predicates', title: '断言器'},
                    {field: 'filters', title: '过滤器'},
                    {field: 'orderNum', title: '顺序号', align: 'center', width: 100},
                    {
                        title: '是否启用', width: 100, align: 'center', templet: function (d) {
                            return d.enabled ? '<span class="layui-badge layui-bg-green"><b>启用</b></span>' : '<span class="layui-badge"><b>禁用</b></span>'
                        }
                    }
                    <@select>
                    , {fixed: 'right', title: '操作', align: "center", toolbar: '#grid-bar', width: 150}
                    </@select>
                ]]
            });

            table.on('toolbar(grid)', function (obj) {
                if (obj.event === 'add') {
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-add-1"></i>&nbsp;新增路由',
                        content: 'toadd',
                        area: ['920px', '700px'],
                        btn: admin.BUTTONS,
                        resize: false,
                        yes: function (index, layero) {
                            const iframeWindow = window['layui-layer-iframe' + index], submitID = 'btn_confirm',
                                submit = layero.find('iframe').contents().find('#' + submitID);
                            iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                                const field = data.field;
                                admin.post('add', field, function () {
                                    table.reload('grid');
                                    updateStatus(true);
                                    layer.close(index);
                                }, function (result) {
                                    admin.error(admin.OPT_FAILURE, result.error);
                                });
                            });
                            submit.trigger('click');
                        }
                    });
                } else if (obj.event === 'working') {
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-read"></i>&nbsp;查看正在工作路由',
                        content: 'toworking',
                        shadeClose: true,
                        shade: 0.8,
                        area: ['90%', '78%']
                    });
                } else if (obj.event === 'del') {
                    const checkedId = admin.getCheckedData(table, obj, "id");
                    if (checkedId.length > 0) {
                        layer.confirm(admin.DEL_QUESTION, function (index) {
                            admin.post("del", {'ids': checkedId.join(",")}, function () {
                                admin.closeDelete(table, obj, index);
                                updateStatus(true);
                            });
                        });
                    } else {
                        admin.error(admin.SYSTEM_PROMPT, admin.DEL_ERROR);
                    }
                } else if (obj.event === 'publish') {
                    if (!$("#btnPublish").hasClass("layui-btn-disabled")) {
                        layer.confirm("确定要发布路由吗?", function (index) {
                            admin.post("publish", {}, function () {
                                $("#search").click();
                                updateStatus(false);
                                admin.success("系统提示", "路由发布成功, 已立即生效");
                                layer.close(index);
                            });
                        });
                    }
                }
            });

            table.on('tool(grid)', function (obj) {
                const data = obj.data;
                if (obj.event === 'edit') {
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-edit" style="color: #1E9FFF;"></i>&nbsp;编辑路由',
                        content: 'toedit?id=' + data.id,
                        area: ['920px', '700px'],
                        btn: admin.BUTTONS,
                        resize: false,
                        yes: function (index, layero) {
                            const iframeWindow = window['layui-layer-iframe' + index], submitID = 'btn_confirm',
                                submit = layero.find('iframe').contents().find('#' + submitID);
                            iframeWindow.layui.form.on('submit(' + submitID + ')', function (d) {
                                const field = d.field;
                                field.id = data.id;
                                admin.post('edit', field, function () {
                                    table.reload('grid');
                                    updateStatus(true);
                                    layer.close(index);
                                }, function (result) {
                                    admin.error(admin.OPT_FAILURE, result.error);
                                });
                            });
                            submit.trigger('click');
                        }
                    });
                } else if (obj.event === 'disable') {
                    layer.confirm("确定要禁用路由吗", function (index) {
                        admin.post('disable', {"id": data.id}, function () {
                            table.reload('grid');
                            updateStatus(true);
                            layer.close(index);
                        }, function (result) {
                            admin.error(admin.OPT_FAILURE, result.error);
                        });
                    });
                } else if (obj.event === 'enable') {
                    layer.confirm("确定要启用路由吗", function (index) {
                        admin.post('enable', {"id": data.id}, function () {
                            table.reload('grid');
                            updateStatus(true);
                            layer.close(index);
                        }, function (result) {
                            admin.error(admin.OPT_FAILURE, result.error);
                        });
                    });
                }
            });

            function updateStatus(update) {
                isUpdate = update;
                if (isUpdate) {
                    enablePublish();
                } else {
                    disablePublish();
                }
            }

            function enablePublish() {
                $("#btnPublish").removeClass("layui-btn-disabled");
                $("#spanStatus").show();
            }

            function disablePublish() {
                $("#btnPublish").addClass("layui-btn-disabled");
                $("#spanStatus").hide();
            }
        });
    </script>
    </body>
    </html>
</@compress>