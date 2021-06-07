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
                <input type="text" id="username" name="username" lay-verify="required" autocomplete="off"
                       placeholder="请填写账号名进行搜索" class="layui-input">
                <input type="button" lay-filter="btnSearch" id="btnSearch" value="搜索">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">姓名</label>
            <div class="layui-input-inline" style="width: 730px">
                <input type="text" id="name" name="name" value="" lay-verify="required" autocomplete="off"
                       class="layui-input layui-disabled" readonly>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">手机</label>
            <div class="layui-input-inline" style="width: 730px">
                <input type="text" id="phoneNumber" name="phoneNumber" lay-verify="phone"
                       class="layui-input layui-disabled" readonly>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">邮箱</label>
            <div class="layui-input-inline" style="width: 730px">
                <input type="text" id="email" name="email" value="" lay-verify="email"
                       class="layui-input layui-disabled" readonly>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block" style="width: 730px">
                <textarea id="description" name="description" class="layui-textarea layui-disabled" style="resize: none" readonly></textarea>
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const admin = layui.admin, $ = layui.$;
            parent.layer.iframeAuto(parent.layer.getFrameIndex(window.name));

            $('#username').keydown(function (e) {
                if ($('#username').val() !== '') {
                    const curKey = e.which;
                    if (curKey == 13) {
                        $('#btnSearch').click();
                    }
                }
            });

            $('#btnSearch').click(function () {
                admin.post('do-search', {'keyword': $('#username').val()}, function (result) {
                    const list = result.data;
                    if (list && list.length > 0) {
                        const item = list[0];
                        $('#username').val(item.username);
                        $('#name').val(item.name);
                        $('#phoneNumber').val(item.phoneNumber);
                        $('#email').val(item.email);
                        $('#description').html(item.description);
                    }
                });
            });
        });
    </script>
    </body>
    </html>
</@compress>