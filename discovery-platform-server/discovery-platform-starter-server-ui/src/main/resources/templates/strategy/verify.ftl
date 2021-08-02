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
            <label class="layui-form-label">校验参数</label>
            <div class="layui-input-inline" style="width: 400px">
                <input type="text" id="txtVerify" class="layui-input" placeholder="请输入待校验的参数，格式示例：a=1;b=1" autocomplete="off">
            </div>
            <div class="layui-input-inline" style="width: 50px;margin-top: 3px;">
                <button class="layui-btn layui-btn-sm layui-btn-normal" id="btnVerify">
                    <i class="layui-icon">&#x1005;</i>&nbsp;校验
                </button>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">校验结果</label>
            <div id="ok" class="layui-input-inline" style="width: 100px;margin-top: 10px;display: none">
                <label class="layui-badge layui-bg-green">
                    <i class="layui-icon layui-icon-ok"></i>&nbsp;成功
                </label>
            </div>
            <div id="fail" class="layui-input-inline" style="width: 100px;margin-top: 10px;display: none">
                <label class="layui-badge layui-bg-red">
                    <i class="layui-icon layui-icon-close"></i>&nbsp;失败
                </label>
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const $ = layui.$, admin = layui.admin;
            $('#btnVerify').click(function () {
                admin.post("validate-expression", {'expression': "${expression}", 'validation': $('#txtVerify').val()}, function (result) {
                    if (result.data) {
                        $('#ok').show();
                        $('#fail').hide();
                    } else {
                        $('#ok').hide();
                        $('#fail').show();
                    }
                });
            });
        });
    </script>
    </body>
    </html>
</@compress>