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
                <div class="layui-inline">角色名称</div>
                <div class="layui-inline">
                    <select name="sysRoleId">
                        <option value="">请选择角色</option>
                        <#list roles as role>
                            <option value="${role.id}">${role.name}</option>
                        </#list>
                    </select>
                </div>
                <div class="layui-inline">菜单名称</div>
                <div class="layui-inline">
                    <select name="sysMenuId">
                        <option value="">请选择菜单</option>
                        <#list menus as menu>
                            <option value="${menu.id}">${menu.name}</option>
                        </#list>
                    </select>
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
                            <i class="layui-icon layui-icon-add-1"></i>&nbsp;&nbsp;新增权限
                        </button>
                    </@insert>
                    <@delete>
                        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="batchDel">
                            <i class="layui-icon layui-icon-delete"></i>&nbsp;&nbsp;删除权限
                        </button>
                    </@delete>
                </div>
            </script>

            <script type="text/html" id="grid-bar">
                <@update>
                    <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i
                                class="layui-icon layui-icon-edit"></i>编辑</a>
                </@update>
                <@delete>
                    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i
                                class="layui-icon layui-icon-delete"></i>删除</a>
                </@delete>
            </script>

            <script type="text/html" id="canInsert">
                <input
                    <@no_update>
                        disabled="disabled"
                    </@no_update>
                        type="checkbox" name="insert" lay-skin="switch" lay-text="有|无" lay-filter="editPermission"
                        value="{{ d.canInsert }}" data-json="{{ encodeURIComponent(JSON.stringify(d)) }}" {{
                        d.canInsert== 1 ? 'checked' : '' }}>
            </script>
            <script type="text/html" id="canDelete">
                <input
                    <@no_update>
                        disabled="disabled"
                    </@no_update>
                        type="checkbox" name="delete" lay-skin="switch" lay-text="有|无" lay-filter="editPermission"
                        value="{{ d.canDelete }}" data-json="{{ encodeURIComponent(JSON.stringify(d)) }}" {{
                        d.canDelete== 1 ? 'checked' : '' }}>
            </script>
            <script type="text/html" id="canUpdate">
                <input
                    <@no_update>
                        disabled="disabled"
                    </@no_update>
                        type="checkbox" name="update" lay-skin="switch" lay-text="有|无" lay-filter="editPermission"
                        value="{{ d.canUpdate }}" data-json="{{ encodeURIComponent(JSON.stringify(d)) }}" {{
                        d.canUpdate== 1 ? 'checked' : '' }}>
            </script>
            <script type="text/html" id="canSelect">
                <input
                    <@no_update>
                        disabled="disabled"
                    </@no_update>
                        type="checkbox" name="select" lay-skin="switch" lay-text="有|无" lay-filter="editPermission"
                        value="{{ d.canSelect }}" data-json="{{ encodeURIComponent(JSON.stringify(d)) }}" {{
                        d.canSelect== 1 ? 'checked' : '' }}>
            </script>
        </div>

    </div>
</div>
<script type="text/javascript">
    layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
        const $ = layui.$, admin = layui.admin, form = layui.form, table = layui.table;
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
            even: true,
            cols: [[
                {type: 'checkbox'},
                {field: 'roleName', title: '角色名称'},
                {field: 'menuName', title: '页面名称'},
                {field: 'canInsert', title: '新增权限', unresize: true, templet: '#canInsert', width: 100},
                {field: 'canDelete', title: '删除权限', unresize: true, templet: '#canDelete', width: 100},
                {field: 'canUpdate', title: '修改权限', unresize: true, templet: '#canUpdate', width: 100},
                {field: 'canSelect', title: '查询权限', unresize: true, templet: '#canSelect', width: 100}
            ]]
        });

        form.on('switch(editPermission)', function (obj) {
            let json = JSON.parse(decodeURIComponent($(this).data('json')));
            json = table.clearCacheKey(json);
            admin.post("do-update", {id: json.id, type: this.name, hasPermission: obj.elem.checked});
        });

        table.on('toolbar(grid)', function (obj) {
            const checkStatus = table.checkStatus(obj.config.id);
            switch (obj.event) {
                case 'batchDel':
                    const data = checkStatus.data;
                    if (data.length > 0) {
                        layer.confirm(admin.DEL_QUESTION, function (index) {
                            let keys = "";
                            let j = 0, len = data.length;
                            for (; j < len; j++) {
                                keys = keys + data[j].id + ","
                            }
                            admin.post('do-delete', {ids: keys}, function () {
                                table.reload('grid');
                                layer.close(index);
                            });
                        });
                    } else {
                        admin.error(admin.SYSTEM_PROMPT, admin.DEL_ERROR);
                    }
                    break;
                case 'add':
                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-add-1" style="color: #009688;"></i>&nbsp;新增权限',
                        content: 'add',
                        area: ['440px', '440px'],
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
        });
    });
</script>
</body>
</html>
