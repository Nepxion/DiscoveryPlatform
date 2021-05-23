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
                    <div class="layui-card-header">设置我的资料</div>
                    <div class="layui-card-body" pad15>
                        <div class="layui-form" lay-filter="">
                            <div class="layui-form-item">
                                <label class="layui-form-label">用户名</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="username" value="${admin.username}" readonly
                                           class="layui-input">
                                </div>
                                <div class="layui-form-mid layui-word-aux">不可修改。用于后台登入名</div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">姓名</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="name" value="${admin.name}" lay-verify="nickname"
                                           autocomplete="off" placeholder="请输入姓名" class="layui-input">
                                </div>
                            </div>

                            <div class="layui-form-item">
                                <label class="layui-form-label">手机</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="phoneNumber" value="${admin.phoneNumber}"
                                           lay-verify="phone"
                                           autocomplete="off"
                                           class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <label class="layui-form-label">邮箱</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="email" value="${admin.email}" lay-verify="email"
                                           autocomplete="off"
                                           class="layui-input">
                                </div>
                            </div>
                            <div class="layui-form-item layui-form-text">
                                <label class="layui-form-label">备注</label>
                                <div class="layui-input-block">
                                    <textarea name="remark" placeholder="请输入备注"
                                              class="layui-textarea">${admin.remark}</textarea>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <div class="layui-input-block">
                                    <button class="layui-btn" lay-submit lay-filter="btnSubmit">确认修改</button>
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
                admin.post("do-edit-info", obj.field, function () {
                    admin.success("系统提示", "个人信息更新成功", function () {
                        top.window.location.reload();
                    });
                })
            });
        });
    </script>
    </body>
    </html>
</@compress>