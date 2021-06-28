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
            <label class="layui-form-label" style="width: 90px">Client Id</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" id="clientId" name="clientId" lay-verify="required" class="layui-input"
                       placeholder="请输入Client Id" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" style="width: 90px">Client Secret</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" id="clientSecret" name="clientSecret" lay-verify="required" class="layui-input"
                       placeholder="请输入Client Secret" autocomplete="off">
                <button id="btnGenName" class="layui-btn layui-btn-sm" style="margin-top: 2px">自动生成
                </button>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" style="width: 90px">应用名称</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" name="name" lay-verify="required" class="layui-input"
                       placeholder="请输入应用名称" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" style="width: 90px">是否管理员</label>
            <div class="layui-input-inline">
                <input type="radio" name="isAdmin" value="true" title="是">
                <input type="radio" name="isAdmin" value="false" title="否" checked>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" style="width: 90px">描述信息</label>
            <div class="layui-input-inline" style="width: 740px">
                <textarea name="description" placeholder="请输入描述信息" class="layui-textarea"
                          style="resize: none;height:300px"></textarea>
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const $ = layui.$, admin = layui.admin;
            $("#btnGenName").click(function () {
                admin.post('do-gen-name', {}, function (data) {
                    $("#clientId").val(data.data.clientId);
                    $("#clientSecret").val(data.data.clientSecret);
                });
            });
        });
    </script>
    </body>
    </html>
</@compress>