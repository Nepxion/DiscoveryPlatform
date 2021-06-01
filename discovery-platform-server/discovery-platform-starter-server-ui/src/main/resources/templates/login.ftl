<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "common/layui.ftl">
        <link rel="stylesheet" href="${ctx}/layuiadmin/style/login.css" media="all">
    </head>
    <body>

    <div class="layadmin-user-login layadmin-user-display-show" id="LAY-user-login" style="display: none;">

        <div class="layadmin-user-login-main">
            <div class="layadmin-user-login-box layadmin-user-login-header">

                <img src="${ctx}/images/Logo.png"/>
                <h2>${fullName}</h2>
                <p>Anytime, Anywhere, Just Enjoy</p>
            </div>
            <div class="layadmin-user-login-box layadmin-user-login-body layui-form">
                <div class="layui-form-item">
                    <label class="layadmin-user-login-icon layui-icon layui-icon-username" for="username"></label>
                    <input type="text" name="username" id="username" lay-verify="required" placeholder="请输入用户名"
                           class="layui-input" autofocus="autofocus">
                </div>
                <div class="layui-form-item">
                    <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="password"></label>
                    <input type="password" name="password" id="password" lay-verify="required" placeholder="请输入密码"
                           class="layui-input">
                </div>
                <div class="layui-form-item" style="margin-bottom: 20px;">

                </div>
                <div class="layui-form-item">
                    <button id="btnSubmit" name="btnSubmit" class="layui-btn layui-btn-fluid" lay-submit
                            lay-filter="btnSubmit">登 录
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div class="layui-trans layadmin-user-login-footer">
        <p>${year}-2050 © <a href="http://www.nepxion.com" target="_blank">Nepxion Studio</a> Apache License</p>
        <p>
          <a href="https://github.com/Nepxion" tppabs="#" target="_blank">GitHub代码</a>
          <a href="https://gitee.com/Nepxion" tppabs="#" target="_blank">Gitee代码</a>
          <a href="https://search.maven.org/search?q=g:com.nepxion" tppabs="#" target="_blank">Maven仓库</a>
          <a href="http://nepxion.gitee.io/discovery/docs/zxing-doc/Pegasus.jpg" tppabs="#" target="_blank">微信</a>
          <a href="http://nepxion.gitee.io/discovery/docs/zxing-doc/%E9%92%89%E9%92%89.jpg" tppabs="#" target="_blank">钉钉</a>
          <a href="http://nepxion.gitee.io/discovery/docs/zxing-doc/%E5%85%AC%E4%BC%97%E5%8F%B7.jpg" tppabs="#" target="_blank">公众号</a>
          <a href="mailto:zhangningkid@163.com" tppabs="#">邮箱</a>
         </p>
        <p>版本:&nbsp;${version}</p>
    </div>

    <script src="${ctx}/layuiadmin/layui/layui.js"></script>
    <script>
        if (window != top) {
            top.location.href = location.href;
        }
        layui.config({base: '../../layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'user'], function () {
            const $ = layui.$, admin = layui.admin, form = layui.form;
            form.render();
            form.on('submit(btnSubmit)', function (obj) {
                obj.field.remember = false;
                admin.post('do-login', obj.field, function () {
                    layer.msg('登录成功', {
                        offset: '15px',
                        icon: 1,
                        time: 500
                    }, function () {
                        location.href = '${ctx}/index';
                    });
                }, function (d) {
                    layer.msg(d.error, {
                        offset: '15px',
                        icon: 2,
                        time: 3000
                    }, function () {
                        $('#password').val('');
                        $('#username').focus();
                    });
                });
            });

            $(document).keydown(function (event) {
                if (event.keyCode === 13) {
                    $('#btnSubmit').trigger('click');
                    return false;
                }
            });
        });
    </script>
    </body>
    </html>
</@compress>