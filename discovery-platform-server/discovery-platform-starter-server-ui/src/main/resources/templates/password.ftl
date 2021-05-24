<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "common/layui.ftl">
    </head>
    <body>

    <div class="layui-fluid">
        <div class="layui-row layui-col-space15">
            <div class="layui-col-md12">
                <div class="layui-card">
                    <div class="layui-card-header">修改密码</div>
                    <div class="layui-card-body" pad15>
                        <div class="layui-form" lay-filter="">
                            <div class="layui-form-item">
                                <label class="layui-form-label">当前密码</label>
                                <div class="layui-input-inline">
                                    <input type="password" name="oldPassword" lay-verify="required" lay-verType="tips"
                                           class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">新密码</label>
                                <div class="layui-input-inline">
                                    <input type="password" name="password" lay-verify="required" autocomplete="off"
                                           id="password" class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">确认新密码</label>
                                <div class="layui-input-inline">
                                    <input type="password" name="repassword" lay-verify="required" autocomplete="off"
                                           class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-input-block">
                                    <button class="layui-btn" lay-submit lay-filter="btnSubmit">确认修改
                                    </button>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="${ctx}/layuiadmin/layui/layui.js"></script>
    <script>
        layui.config({base: '../../layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'user'], function () {
            const admin = layui.admin, form = layui.form;
            form.render();
            form.on('submit(btnSubmit)', function (obj) {
                if (obj.field.password !== obj.field.repassword) {
                    admin.error("系统提示", "输入的密码不一致，请重新输入");
                } else {
                    admin.post("do-change-password", obj.field, function () {
                        admin.success("系统提示", "密码修改成功，请重新登录", function () {
                            admin.quit();
                        })
                    });
                }
            });
        });
    </script>
    </body>
    </html>
</@compress>