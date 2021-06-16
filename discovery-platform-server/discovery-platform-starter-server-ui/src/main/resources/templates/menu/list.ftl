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
                <div class="layui-inline">页面名称</div>
                <div class="layui-inline" style="width:500px">
                    <input type="text" name="name" placeholder="请输入页面名称" autocomplete="off" class="layui-input">
                </div>
                <div class="layui-inline">
                    <button class="layui-btn layuiadmin-btn-admin" lay-submit lay-filter="search">
                        <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                    </button>
                </div>
            </div>
        </div>

        <div class="layui-card-body">
            <table class="layui-hide" id="grid" lay-filter="grid"></table>
            <script type="text/html" id="grid-toolbar">
                <div class="layui-btn-container">
                    <@insert>
                        <button class="layui-btn layui-btn-sm layuiadmin-btn-admin" lay-event="add">
                            <i class="layui-icon layui-icon-add-1"></i>&nbsp;&nbsp;新增页面
                        </button>
                    </@insert>
                    <@delete>
                        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="batchDel">
                            <i class="layui-icon layui-icon-delete"></i>&nbsp;&nbsp;删除页面
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
<script type="text/javascript">
    layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
        const admin = layui.admin, form = layui.form, table = layui.table;
        tableErrorHandler();
        form.on('submit(search)', function (data) {
            const field = data.field;
            table.reload('grid', {page: {curr: 1}, where: field});
        });
        table.render({
            elem: '#grid',
            url: 'do-list',
            toolbar: '#grid-toolbar',
            method: 'post',
            cellMinWidth: 80,
            page: true,
            limit: 15,
            limits: [15],
            even: true,
            cols: [[
                {type: 'checkbox'},
                {type: 'numbers', title: '序号'},
                {field: 'name', title: '页面名称', width: 150},
                {field: 'parentName', title: '父级菜单', width: 150},
                {field: 'url', title: 'URL地址', width: 250},
                {field: 'order', title: '排序号', width: 150},
                {
                    title: '菜单图标', width: 100, templet: function (d) {
                        return '<i class="layui-icon ' + d.iconClass + '" style="color: #1E9FFF;"></i>';
                    }
                },
                {
                    title: '是否是菜单', width: 120, templet: function (d) {
                        return d.showFlag ? '是' : '否'
                    }
                },
                {
                    title: '是否首页', width: 120, templet: function (d) {
                        return d.defaultFlag ? '是' : '否'
                    }
                },
                {
                    title: '是否新窗口', width: 120, templet: function (d) {
                        return d.blankFlag ? '是' : '否'
                    }
                },
                {field: 'description', title: '描述信息'}
                <@select>
                , {fixed: 'right', title: '操作', align: 'center', toolbar: '#grid-bar', width: 90}
                </@select>
            ]]
        });

        table.on('toolbar(grid)', function (obj) {
            switch (obj.event) {
                case 'batchDel':
                    const checkedId = admin.getCheckedData(table, obj, "id");
                    if (checkedId.length > 0) {
                        layer.confirm(admin.DEL_QUESTION, function (index) {
                            admin.post('do-delete', {'ids': checkedId.join(",")}, function () {
                                admin.closeDelete(table, obj, index);
                            });
                        });
                    } else {
                        admin.error(admin.SYSTEM_PROMPT, admin.DEL_ERROR);
                    }
                    break;
                case 'add':
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-add-1"></i>&nbsp;添加页面',
                        content: 'add',
                        area: ['520px', '620px'],
                        btn: admin.BUTTONS,
                        resize: false,
                        yes: function (index, layero) {
                            const iframeWindow = window['layui-layer-iframe' + index], submitID = 'btn_confirm',
                                submit = layero.find('iframe').contents().find('#' + submitID);
                            iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                                const field = data.field;
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
                    break;
            }
            ;
        });

        table.on('tool(grid)', function (obj) {
            const data = obj.data;
            if (obj.event = 'edit') {
                layer.open({
                    type: 2,
                    title: '<i class="layui-icon layui-icon-edit"></i>&nbsp;编辑页面',
                    content: 'edit?id=' + data.id,
                    area: ['520px', '620px'],
                    btn: admin.BUTTONS,
                    resize: false,
                    yes: function (index, layero) {
                        const iframeWindow = window['layui-layer-iframe' + index], submitID = 'btn_confirm',
                            submit = layero.find('iframe').contents().find('#' + submitID);
                        iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                            const field = data.field;
                            admin.post('do-update', admin.toJson(field), function () {
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
