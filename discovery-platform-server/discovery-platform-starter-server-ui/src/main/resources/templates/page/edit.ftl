<!DOCTYPE html>
<html>
<head>
    <#include "../common/layui.ftl">
</head>
<body>
<div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin" style="padding: 20px 30px 0 0;">
    <input value="${page.id}" type="hidden" name="id" lay-verify="required"/>
    <div class="layui-form-item">
        <label class="layui-form-label">页面名称</label>

        <div class="layui-input-inline" style="width:250px">
            <input value="${page.name}" placeholder="请填写页面地址" type="text" name="name" lay-verify="required"
                   autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">URL地址</label>
        <div class="layui-input-inline" style="width:250px">
            <input value="${page.url}" placeholder="请填写URL地址" type="text" name="url" autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><a href="https://www.layui.com/doc/element/icon.html" target="_blank"
                                           title="点击可查看菜单图标帮助文档"><i>菜单图标</i></a></label>
        <div class="layui-input-inline" style="width:250px">
            <input value="${page.iconClass}" placeholder="请填写菜单图标" type="text" name="iconClass" autocomplete="off"
                   class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">父级菜单</label>
        <div class="layui-input-inline" style="width:250px">
            <select name="parentId" lay-verify="" style="z-index: 99999">
                <option value="0"> - 请选择顶级菜单 -</option>
                <#list pages as p>
                    <option value="${p.id}" <#if p.id==page.parentId>selected="selected"</#if>>${p.name}</option>
                </#list>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">是否是菜单</label>
        <div class="layui-input-inline">
            <input <#if page.isMenu==true>checked="checked"</#if> type="checkbox" name="isMenu" lay-verify="required"
                   class="layui-input" title="菜单">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">是否首页</label>
        <div class="layui-input-inline">
            <input <#if page.isDefault==true>checked="checked"</#if> type="checkbox" name="isDefault"
                   lay-verify="required" class="layui-input" title="首页">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">是否新窗口</label>
        <div class="layui-input-inline">
            <input <#if page.isBlank==true>checked="checked"</#if> type="checkbox" name="isBlank" lay-verify="required"
                   class="layui-input" title="新窗口">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">排序号</label>
        <div class="layui-input-inline" style="width:250px">
            <input value="${page.order}" placeholder="请填写排序号（值越小,越靠前）" type="text" name="order"
                   lay-verify="required|number" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">描述信息</label>
        <div class="layui-input-inline" style="width:250px">
            <textarea name="remark" placeholder="请填写描述信息" class="layui-textarea"
                      style="resize: none">${page.remark}</textarea>
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
