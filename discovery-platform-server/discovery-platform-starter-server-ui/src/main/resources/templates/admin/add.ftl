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
                        <option value="${role.id}">${role.name}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">用户名</label>
            <div class="layui-input-inline" style="width: 730px">
                <input type="text" name="username" lay-verify="required" class="layui-input"
                       placeholder="请输入用户名" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">密码</label>
            <div class="layui-input-inline" style="width: 730px">
                <input type="password" id="password" name="password" lay-verify="required"
                       placeholder="请输入密码" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">确认密码</label>
            <div class="layui-input-inline" style="width: 730px">
                <input type="password" id="repassword" name="repassword" lay-verify="required"
                       placeholder="请确认密码" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">姓名</label>
            <div class="layui-input-inline" style="width: 730px">
                <input type="text" name="name" value="" lay-verify="required" autocomplete="off"
                       placeholder="请输入姓名" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">手机</label>
            <div class="layui-input-inline" style="width: 730px">
                <input type="text" name="phoneNumber" lay-verify="phone"
                       autocomplete="off" class="layui-input" placeholder="请输入手机号">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">邮箱</label>
            <div class="layui-input-inline" style="width: 730px">
                <input type="text" name="email" value="" lay-verify="email" autocomplete="off"
                       class="layui-input" placeholder="请输入邮箱">
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block" style="width: 730px">
                <textarea name="description" placeholder="请输入备注" class="layui-textarea" style="resize: none"></textarea>
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