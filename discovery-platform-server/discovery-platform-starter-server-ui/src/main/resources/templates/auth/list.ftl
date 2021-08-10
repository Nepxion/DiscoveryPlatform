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
                    <div class="layui-inline">应用名称</div>
                    <div class="layui-inline" style="width:500px">
                        <select name="appId" lay-filter="appId" lay-search>
                            <option value="">请选择应用名称</option>
                            <#list apps as app >
                                <option value="${app.id}">${app.name}</option>
                            </#list>
                        </select>
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
                                <i class="layui-icon layui-icon-add-1"></i>&nbsp;&nbsp;新增权限
                            </button>
                        </@insert>
                        <@delete>
                            <button class="layui-btn layui-btn-sm layui-btn-danger"
                                    lay-event="del">
                                <i class="layui-icon layui-icon-delete"></i>&nbsp;&nbsp;删除权限
                            </button>
                        </@delete>
                    </div>
                </script>
            </div>
        </div>
    </div>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
            const $ = layui.$, admin = layui.admin, form = layui.form, table = layui.table;
            let appId = null;
            tableErrorHandler();
            form.on('submit(search)', function (data) {
                const field = data.field;
                table.reload('grid', {page: {curr: 1}, where: field});
            });
            table.render({
                elem: '#grid',
                url: 'list',
                toolbar: '#grid-toolbar',
                defaultToolbar: [],
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
                ]]
            });

            table.on('toolbar(grid)', function (obj) {
                if (obj.event === 'add') {
                    if (!appId) {
                        admin.error('系统提示', '请先选择应用名称');
                        return;
                    }
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-add-1" style="color: #009688;"></i>&nbsp;新增权限',
                        content: 'toadd?appId=' + appId,
                        area: ['1200px', '98%'],
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
                    const checkStatus = table.checkStatus(obj.config.id);
                    const data = checkStatus.data;
                    if (data.length > 0) {
                        layer.confirm(admin.DEL_QUESTION, function (index) {
                            let keys = "";
                            for (let j = 0, len = data.length; j < len; j++) {
                                keys = keys + data[j].id + ","
                            }
                            admin.post('do-delete', {'appId': appId, 'apiIds': keys}, function () {
                                table.reload('grid');
                                layer.close(index);
                            });
                        });
                    } else {
                        admin.error(admin.SYSTEM_PROMPT, admin.DEL_ERROR);
                    }
                }
            });

            form.on('select(appId)', function (data) {
                appId = data.value;
                $('#search').click();
            });
        });
    </script>
    </body>
    </html>
</@compress>