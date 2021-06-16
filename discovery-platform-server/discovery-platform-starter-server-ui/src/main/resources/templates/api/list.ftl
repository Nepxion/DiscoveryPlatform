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
                    <div class="layui-inline">接口名称</div>
                    <div class="layui-inline" style="width:200px">
                        <input type="text" name="name" placeholder="请输入接口名称" autocomplete="off"
                               class="layui-input">
                    </div>

                    <div class="layui-inline">服务名称</div>
                    <div class="layui-inline" style="width:200px">
                        <select name="serviceName" lay-filter="serviceName" lay-search>
                            <option value="">请选择服务名称</option>
                            <#list serviceNames as serviceName >
                                <option value="${serviceName}">${serviceName}</option>
                            </#list>
                        </select>
                    </div>

                    <div class="layui-inline">接口路径</div>
                    <div class="layui-inline" style="width:200px">
                        <input type="text" name="path" placeholder="请输入接口路径" autocomplete="off"
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
                                    lay-event="add">
                                <i class="layui-icon layui-icon-add-1"></i>&nbsp;&nbsp;新增接口
                            </button>
                        </@insert>
                        <@delete>
                            <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">
                                <i class="layui-icon layui-icon-delete"></i>&nbsp;&nbsp;删除接口
                            </button>
                        </@delete>
                    </div>
                </script>

                <script type="text/html" id="grid-bar">
                    <@update>
                        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i
                                    class="layui-icon layui-icon-edit"></i>编辑</a>
                    </@update>
                </script>
            </div>
        </div>
    </div>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
            const admin = layui.admin, form = layui.form, table = layui.table;
            tableErrorHandler();
            form.on('submit(search)', function (data) {
                const field = data.field;
                table.reload('grid', {page: {curr: 1}, where: field});
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
                    {field: 'serviceName', title: '服务名称', width: 250},
                    {field: 'name', title: '接口名称', width: 400},
                    {field: 'method', title: '请求方式', width: 300},
                    {field: 'path', title: '接口路径'}
                    <@select>
                    , {fixed: 'right', title: '操作', align: 'center', toolbar: '#grid-bar', width: 80}
                    </@select>
                ]]
            });

            table.on('toolbar(grid)', function (obj) {
                if (obj.event === 'add') {
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-add-1"></i>&nbsp;新增接口',
                        content: 'toadd',
                        area: ['910px', '98%'],
                        btn: admin.BUTTONS,
                        resize: false,
                        yes: function (index, layero) {
                            const iframeWindow = window['layui-layer-iframe' + index], submitID = 'btn_confirm',
                                submit = layero.find('iframe').contents().find('#' + submitID);
                            iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                                const field = data.field;
                                admin.post('add', field, function () {
                                    table.reload('grid');
                                    layer.close(index);
                                }, function (result) {
                                    admin.error(admin.OPT_FAILURE, result.error);
                                });
                            });
                            submit.trigger('click');
                        }
                    });
                } else if (obj.event === 'del') {
                    const checkedId = admin.getCheckedData(table, obj, 'id');
                    if (checkedId.length > 0) {
                        layer.confirm(admin.DEL_QUESTION, function (index) {
                            admin.post('do-delete', {'ids': checkedId.join(',')}, function () {
                                admin.closeDelete(table, obj, index);
                            });
                        });
                    } else {
                        admin.error(admin.SYSTEM_PROMPT, admin.DEL_ERROR);
                    }
                }
            });

            table.on('tool(grid)', function (obj) {
                const data = obj.data;
                if (obj.event === 'edit') {
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-edit" style="color: #1E9FFF;"></i>&nbsp;编辑接口',
                        content: 'toedit?id=' + data.id,
                        area: ['910px', '98%'],
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
                                    layer.close(index);
                                }, function (result) {
                                    admin.error(admin.OPT_FAILURE, result.error);
                                });
                            });
                            submit.trigger('click');
                        }
                    });
                }
            });
        });
    </script>
    </body>
    </html>
</@compress>