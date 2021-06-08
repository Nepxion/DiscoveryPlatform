<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
    </head>
    <body>

    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin"
         style="padding: 20px 30px 0 0;">

        <div class="layui-form-item">
            <label class="layui-form-label">角色</label>
            <div class="layui-input-inline" style="width: 730px">
                <select name="roleId" lay-filter="roleId" lay-verify="required" lay-search>
                    <option value="">请选择角色</option>
                    <#list roles as role >
                        <option value="${role.id}"
                                ${(admin.sysRoleId==role.id)?string('selected="selected"','')}>${role.name}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">用户名</label>
            <div class="layui-input-inline" style="width: 550px">
                <input type="text" name="username" lay-verify="required" class="layui-input layui-disabled"
                       value="${admin.username}" readonly>
            </div>
            <div class="layui-form-mid layui-word-aux">不可修改。用于后台登录名</div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">姓名</label>
            <div class="layui-input-inline" style="width: 730px">
                <#if loginMode=="DATABASE">
                    <input type="text" name="name" lay-verify="required" class="layui-input"
                           placeholder="请输入姓名" value="${admin.name}" autocomplete="off">
                <#else>
                    <input type="text" name="name" lay-verify="required" class="layui-input layui-disabled"
                           value="${admin.name}" readonly>
                </#if>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">手机</label>
            <div class="layui-input-inline"  style="width: 730px">
                <#if loginMode=="DATABASE">
                    <input type="text" name="phoneNumber" lay-verify="phone" class="layui-input"
                           placeholder="请输入手机号" value="${admin.phoneNumber}" autocomplete="off">
                <#else>
                    <input type="text" name="phoneNumber" lay-verify="phone"
                           class="layui-input layui-disabled" value="${admin.phoneNumber}" readonly>
                </#if>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">邮箱</label>
            <div class="layui-input-inline" style="width: 730px">
                <#if loginMode=="DATABASE">
                    <input type="text" name="email" lay-verify="email" class="layui-input"
                           placeholder="请输入邮箱" value="${admin.email}" autocomplete="off">
                <#else>
                    <input type="text" name="email" lay-verify="email"
                           class="layui-input layui-disabled" value="${admin.email}" readonly>
                </#if>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block" style="width: 730px">
                <#if loginMode=="DATABASE">
                    <textarea name="description" placeholder="请输入备注" class="layui-textarea" style="resize: none">${admin.description}</textarea>
                <#else>
                    <textarea name="description" class="layui-textarea layui-disabled" style="resize: none" readonly>${admin.description}</textarea>
                </#if>
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            parent.layer.iframeAuto(parent.layer.getFrameIndex(window.name));
        });
    </script>
    </body>
    </html>
</@compress>