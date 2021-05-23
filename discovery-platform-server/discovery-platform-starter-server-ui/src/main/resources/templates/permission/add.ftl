<!DOCTYPE html>
<html>
<head>
    <#include "../common/layui.ftl">
</head>
<body>
<div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin" style="padding: 20px 30px 0 0;">
    <div class="layui-form-item">
        <label class="layui-form-label">角色名称</label>
        <div class="layui-input-inline">
            <select id="sysRoleId" name="sysRoleId" lay-filter="sysRoleId" lay-verify="required">
                <option value="">请选择角色</option>
                <#list roles as role>
                    <option value="${role.id}">${role.name}</option>
                </#list>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">页面名称</label>
        <div class="layui-input-inline">
            <select id="sysPageId" name="sysPageId" lay-filter="sysPageId" lay-verify="required">
                <option value="">请选择页面</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">新增权限</label>
        <div class="layui-input-inline">
            <input checked="checked" type="checkbox" name="insert" lay-verify="required" class="layui-input"
                   title="新增权限">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">删除权限</label>
        <div class="layui-input-inline">
            <input checked="checked" type="checkbox" name="delete" lay-verify="required" class="layui-input"
                   title="删除权限">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">修改权限</label>
        <div class="layui-input-inline">
            <input checked="checked" type="checkbox" name="update" lay-verify="required" class="layui-input"
                   title="修改权限">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">查询权限</label>
        <div class="layui-input-inline">
            <input checked="checked" type="checkbox" name="select" lay-verify="required" class="layui-input"
                   title="查询权限">
        </div>
    </div>
    <div class="layui-form-item layui-hide">
        <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
    </div>
</div>
<script type="text/javascript">
    layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
        const admin = layui.admin, $ = layui.$, form = layui.form;
        parent.layer.iframeAuto(parent.layer.getFrameIndex(window.name));

        form.on('select(sysRoleId)', function (data) {
            const sysRoleId = data.value;
            $("select[name=sysPageId]").html("<option value=\"\">请选择页面</option>");
            admin.post("do-get-pages", {'sysRoleId': sysRoleId}, function (res) {
                $.each(res.data, function (key, val) {
                    const option = $("<option>").val(val.id).text(val.name);
                    $("select[name=sysPageId]").append(option);
                });
                $('#sysPageId option:eq(1)').attr('selected', 'selected');
                layui.form.render('select');
            });
        });
    });
</script>
</body>
</html>
