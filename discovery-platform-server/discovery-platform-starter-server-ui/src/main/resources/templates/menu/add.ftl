<!DOCTYPE html>
<html>
<head>
    <#include "../common/layui.ftl">
</head>
<body>
<div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin" style="padding: 20px 30px 0 0;">
    <div class="layui-form-item">
        <label class="layui-form-label">页面名称</label>
        <div class="layui-input-inline" style="width:250px">
            <input type="text" name="name" lay-verify="required" placeholder="请填写页面地址" autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">URL地址</label>
        <div class="layui-input-inline" style="width:250px">
            <input type="text" name="url" placeholder="请填写URL地址" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">
            <a href="https://www.layui.com/doc/element/icon.html" target="_blank" title="点击可查看菜单图标帮助文档"><i>菜单图标</i></a></label>
        <div class="layui-input-inline" style="width:250px">
            <input type="text" name="iconClass" placeholder="请填写菜单图标" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">父级菜单</label>
        <div class="layui-input-inline" style="width:250px">
            <select name="parentId" lay-verify="">
                <option value="0"> - 请选择顶级菜单 -</option>
                <#list menus as m>
                    <option value="${m.id}">${m.name}</option>
                </#list>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">是否是菜单</label>
        <div class="layui-input-inline">
            <input type="checkbox" name="menuFlag" lay-verify="required" class="layui-input" title="菜单" checked="checked">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">是否首页</label>
        <div class="layui-input-inline">
            <input type="checkbox" name="defaultFlag" lay-verify="required" class="layui-input" title="首页">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">是否新窗口</label>
        <div class="layui-input-inline">
            <input type="checkbox" name="blankFlag" lay-verify="required" class="layui-input" title="新窗口">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">描述信息</label>
        <div class="layui-input-inline" style="width:250px">
            <textarea name="description" placeholder="请填写描述信息" class="layui-textarea" style="resize: none"></textarea>
        </div>
    </div>
    <div class="layui-form-item layui-hide">
        <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
    </div>
</div>
<script type="text/javascript">
    layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
        parent.layer.iframeAuto(parent.layer.getFrameIndex(window.name));
    });
</script>
</body>
</html>
