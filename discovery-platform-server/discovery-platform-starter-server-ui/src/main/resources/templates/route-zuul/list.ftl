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

                <script type="text/html" id="colState">

                    {{#  if(d.publishFlag){ }}

                    {{#  if(d.enableFlag){ }}
                    <span class="layui-badge layui-bg-green"><b>已启用</b></span>
                    {{#  } else { }}
                    <span class="layui-badge"><b>已禁用</b></span>
                    {{#  } }}

                    {{#  } else { }}
                    <span class="layui-badge layui-bg-orange"><b>未发布</b></span>
                    {{#  } }}
                </script>

                <script type="text/html" id="colPortalName">
                    {{ d.portalName }} &nbsp;&nbsp;
                    {{#  if(!d.publishFlag){ }}
                    {{#  if(d.operation==1){ }}
                    <span class="layui-badge layui-bg-green"><b>增</b></span>
                    {{#  } else if(d.operation==2){ }}
                    <span class="layui-badge layui-bg-blue"><b>改</b></span>
                    {{#  } else if(d.operation==3){ }}
                    <span class="layui-badge layui-bg-red"><b>删</b></span>
                    {{#  } else { }}
                    <span class="layui-badge layui-bg-red"><b>未知</b></span>
                    {{#  } }}
                    {{#  } }}
                </script>

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
                                        class="layui-icon layui-icon-read"></i>&nbsp;&nbsp;查看正在工作的路由
                            </button>
                        </@select>
                        <@update>
                            <button id="btnPublish"
                                    class="layui-btn-disabled layui-btn layui-btn-sm layui-btn-normal layuiadmin-btn-admin"
                                    lay-event="publish" style="margin-left: 50px">
                                <i class="layui-icon layui-icon-release"></i>&nbsp;&nbsp;发布<b>Zuul</b>路由
                            </button>
                        </@update>
                    </div>
                </script>

                <script type="text/html" id="grid-bar">
                    <@update>
                        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i
                                    class="layui-icon layui-icon-edit"></i>编辑</a>
                        {{#  if(d.enableFlag){ }}
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
            tableErrorHandler();
            form.on('submit(search)', function (data) {
                const field = data.field;
                table.reload('grid', {where: field});
                updateStatusByGrid(table.cache['grid']);
            });

            table.render({
                elem: '#grid',
                url: 'do-list',
                toolbar: '#grid-toolbar',
                method: 'post',
                cellMinWidth: 80,
                page: false,
                limit: 99999999,
                limits: [99999999],
                even: true,
                text: {
                    none: '暂无相关数据'
                },
                cols: [[
                    {type: 'checkbox'},
                    {type: 'numbers', title: '序号', width: 50},
                    {title: '状态', align: 'center', templet: '#colState', width: 80},
                    {title: '网关名称', templet: '#colPortalName', width: 275},
                    {field: 'path', title: '目标地址'},
                    {field: 'url', title: '连接地址', width: 200},
                    {
                        title: '是否过滤', align: 'center', templet: function (d) {
                            return d.stripPrefix ? '是' : '否'
                        }, width: 100
                    },
                    {
                        title: '是否重试', align: 'center', templet: function (d) {
                            return d.retryable ? '是' : '否'
                        }, width: 100
                    },
                    {
                        title: '忽略请求头', templet: function (d) {
                            if (d.sensitiveHeaders.length > 0) {
                                return '';
                            }
                            let r = '';
                            $.each(d.sensitiveHeaders, function (k, v) {
                                r = (r + v + ', ');
                            });
                            if (r != "") {
                                r = r.substr(0, r.length - 2);
                            }
                            return r;
                        }, width: 200
                    },
                    {
                        title: '是否自定义', align: 'center', templet: function (d) {
                            return d.customSensitiveHeaders ? '是' : '否'
                        }, width: 100
                    },
                    {field: 'description', title: '路由描述', width: 150}
                    <@select>
                    , {fixed: 'right', title: '操作', align: 'center', toolbar: '#grid-bar', width: 150}
                    </@select>
                ]],
                done: function (res) {
                    updateStatusByGrid(res.data);
                }
            });

            table.on('toolbar(grid)', function (obj) {
                if (obj.event === 'add') {
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-add-1" style="color: #009688;"></i>&nbsp;新增<b>Zuul</b>路由',
                        content: 'add',
                        area: ['920px', '98%'],
                        btn: admin.BUTTONS,
                        resize: false,
                        yes: function (index, layero) {
                            const iframeWindow = window['layui-layer-iframe' + index], submitID = 'btn_confirm',
                                submit = layero.find('iframe').contents().find('#' + submitID);
                            iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                                const field = data.field;
                                admin.post('do-insert', field, function () {
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
                        title: '<i class="layui-icon layui-icon-read"></i>&nbsp;查看正在工作<b>Zuul</b>路由',
                        content: 'working',
                        shadeClose: true,
                        shade: 0.8,
                        area: ['90%', '82%'],
                        btn: '关闭'
                    });
                } else if (obj.event === 'del') {
                    const checkedId = admin.getCheckedData(table, obj, 'id');
                    if (checkedId.length > 0) {
                        layer.confirm(admin.DEL_QUESTION, function (index) {
                            admin.post('do-delete', {'ids': checkedId.join(",")}, function () {
                                admin.closeDelete(table, obj, index);
                                updateStatus(true);
                            });
                        });
                    } else {
                        admin.error(admin.SYSTEM_PROMPT, admin.DEL_ERROR);
                    }
                } else if (obj.event === 'publish') {
                    if (!$("#btnPublish").hasClass('layui-btn-disabled')) {
                        layer.confirm('确定要发布路由吗？', function (index) {
                            admin.post('do-publish', {}, function () {
                                $("#search").click();
                                updateStatus(false);
                                admin.success('系统提示', '路由发布成功, 已立即生效');
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
                        title: '<i class="layui-icon layui-icon-edit" style="color: #1E9FFF;"></i>&nbsp;编辑<b>Zuul</b>路由',
                        content: 'edit?id=' + data.id,
                        area: ['920px', '98%'],
                        btn: admin.BUTTONS,
                        resize: false,
                        yes: function (index, layero) {
                            const iframeWindow = window['layui-layer-iframe' + index], submitID = 'btn_confirm',
                                submit = layero.find('iframe').contents().find('#' + submitID);
                            iframeWindow.layui.form.on('submit(' + submitID + ')', function (d) {
                                const field = d.field;
                                field.id = data.id;
                                admin.post('do-update', field, function () {
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
                    layer.confirm('确定要禁用路由吗？', function (index) {
                        admin.post('do-disable', {"id": data.id}, function () {
                            table.reload('grid');
                            updateStatus(true);
                            layer.close(index);
                        }, function (result) {
                            admin.error(admin.OPT_FAILURE, result.error);
                        });
                    });
                } else if (obj.event === 'enable') {
                    layer.confirm('确定要启用路由吗？', function (index) {
                        admin.post('do-enable', {"id": data.id}, function () {
                            table.reload('grid');
                            updateStatus(true);
                            layer.close(index);
                        }, function (result) {
                            admin.error(admin.OPT_FAILURE, result.error);
                        });
                    });
                }
            });

            function updateStatusByGrid(data) {
                let needPublish = false;
                $.each(data, function (idx, val) {
                    if (!val.publishFlag) {
                        needPublish = true;
                        return;
                    }
                });
                updateStatus(needPublish);
            }

            function updateStatus(needUpdate) {
                if (needUpdate) {
                    $('#btnPublish').removeClass('layui-btn-disabled');
                } else {
                    $('#btnPublish').addClass('layui-btn-disabled');
                }
            }
        });
    </script>
    </body>
    </html>
</@compress>