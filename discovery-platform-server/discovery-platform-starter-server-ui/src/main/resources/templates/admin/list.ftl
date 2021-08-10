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
                    <div class="layui-inline">管理员姓名</div>
                    <div class="layui-inline" style="width:500px">
                        <input type="text" name="name" placeholder="请输入管理员姓名" autocomplete="off"
                               class="layui-input">
                    </div>
                    <div class="layui-inline">
                        <button class="layui-btn layuiadmin-btn-admin" lay-submit lay-filter="search">
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
                            <button class="layui-btn layui-btn-sm layuiadmin-btn-admin" lay-event="add">
                                <i class="layui-icon layui-icon-add-1"></i>&nbsp;&nbsp;新增管理员
                            </button>
                        </@insert>
                        <@delete>
                            <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="del">
                                <i class="layui-icon layui-icon-delete"></i>&nbsp;&nbsp;删除管理员
                            </button>
                        </@delete>
                    </div>
                </script>

                <script type="text/html" id="grid-bar">
                    <@update>
                        <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i
                                    class="layui-icon layui-icon-edit"></i>编辑</a>
                        <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="resetPassword"><i
                                    class="layui-icon layui-icon-password"></i>密码重置</a>
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
                table.reload('grid', {page: {curr: 1}, where: field});
            });
            table.render({
                elem: '#grid',
                url: 'do-list',
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
                    {field: 'username', title: '登录名', width: 150},
                    {field: 'roleName', title: '角色名', width: 200},
                    {field: 'name', title: '姓名', width: 200},
                    {field: 'phoneNumber', title: '手机号', width: 200},
                    {field: 'email', title: '邮箱地址', width: 200},
                    {field: 'description', title: '备注'}
                    <@select>
                    , {fixed: 'right', title: '操作', align: "center", toolbar: '#grid-bar', width: 180}
                    </@select>
                ]]
            });

            table.on('toolbar(grid)', function (obj) {
                if (obj.event === 'add') {
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-add-1" style="color: #009688;"></i>&nbsp;新增管理员',
                        content: 'add',
                        area: ['910px', '620px'],
                        btn: admin.BUTTONS,
                        resize: false,
                        yes: function (index, layero) {
                            const iframeWindow = window['layui-layer-iframe' + index], submitID = 'btn_confirm',
                                submit = layero.find('iframe').contents().find('#' + submitID);
                            iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                                const field = data.field;
                                if (field.password !== field.repassword) {
                                    admin.error("系统提示", "两次输入的密码不一致", function () {
                                        layero.find('iframe').contents().find('#repassword').val('');
                                        layero.find('iframe').contents().find('#password').val('').focus();
                                    });
                                    return;
                                }
                                admin.post('do-insert', field, function () {
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
                        title: '<i class="layui-icon layui-icon-edit" style="color: #1E9FFF;"></i>&nbsp;编辑管理员',
                        content: 'edit?id=' + data.id,
                        area: ['910px', '510px'],
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
                                    layer.close(index);
                                }, function (result) {
                                    admin.error(admin.OPT_FAILURE, result.error);
                                });
                            });
                            submit.trigger('click');
                        }
                    });
                } else if (obj.event === 'resetPassword') {
                    layer.confirm('确定要重置[' + data.username + ']的密码吗?', function (index) {
                        admin.post('do-reset-password', data, function () {
                                layer.close(index);
                                admin.success('系统提示', '密码重置成功, 初始密码为:admin');
                            }, function () {
                                admin.error('系统提示', '密码重置失败');
                            }
                        );
                    });
                }
            });
        });
    </script>
    </body>
    </html>
</@compress>