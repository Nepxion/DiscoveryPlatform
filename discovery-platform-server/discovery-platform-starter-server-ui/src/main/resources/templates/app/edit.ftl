<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
    </head>
    <body>

    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin">

        <div class="layui-tab layui-tab-brief">
            <ul class="layui-tab-title">
                <li class="layui-this">基本信息</li>
                <li>Access Token</li>
            </ul>
            <div class="layui-tab-content" style="height: 100px">
                <div class="layui-tab-item layui-show">
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 90px">Client Id</label>
                        <div class="layui-input-inline">
                            <input type="text" name="clientId" lay-verify="required" class="layui-input layui-disabled"
                                   style="width: 740px"
                                   autocomplete="off" value="${app.clientId}" readonly="readonly">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 90px">Client Secret</label>
                        <div class="layui-input-inline">
                            <input type="text" name="clientSecret" lay-verify="required"
                                   class="layui-input layui-disabled"
                                   style="width: 740px"
                                   autocomplete="off" value="${app.clientSecret}" readonly="readonly">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 90px">应用名称</label>
                        <div class="layui-input-inline">
                            <input type="text" name="name" lay-verify="required" class="layui-input"
                                   style="width: 740px"
                                   placeholder="请输入应用名称" autocomplete="off" value="${app.name}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 90px">是否管理员</label>
                        <div class="layui-input-inline">
                            <input type="radio" name="isAdmin" value="true"
                                   title="是" ${(app.isAdmin)?string('checked','')}>
                            <input type="radio" name="isAdmin" value="false"
                                   title="否" ${(app.isAdmin)?string('','checked')}>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 90px">描述信息</label>
                        <div class="layui-input-inline">
                            <textarea name="description" placeholder="请输入描述信息" class="layui-textarea"
                                      style="resize: none;width: 740px;height:300px">${app.description}</textarea>
                        </div>
                    </div>
                </div>
                <div class="layui-tab-item">
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 90px">Access Token</label>
                        <div class="layui-input-inline">
                            <textarea id="accessToken" readonly="readonly" class="layui-textarea"
                                      style="width: 740px;height: 100px;resize: none">${(ak.accessToken)!''}</textarea>
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 90px">过期时间(秒)</label>
                        <div class="layui-input-inline">
                            <input type="text" id="expire" readonly="readonly" class="layui-input"
                                   style="width: 740px" value="${(ak.expiresTime)!''}">
                        </div>
                    </div>

                    <div class="layui-form-item">
                        <label class="layui-form-label"></label>
                        <div class="layui-input-inline" style="width:600px">
                            <button id="btnRefresh" class="layui-btn layui-btn-sm">
                                刷新Token
                            </button>
                            <button id="btnRefreshAccessToken" ${((createTokenUrl!'')=='')?string('disabled="disabled"','')}
                                    class="${((createTokenUrl!'')=='')?string('layui-btn-disabled','')} layui-btn layui-btn-sm layui-btn-normal">
                                生成Token
                            </button>
                            <button id="btnLogoutAccessToken" ${((createTokenUrl!'')=='')?string('disabled="disabled"','')}
                                    class="${((createTokenUrl!'')=='')?string('layui-btn-disabled','')} layui-btn layui-btn-sm layui-btn-danger">
                                注销Token
                            </button>
                        </div>
                    </div>

                    <blockquote id="message" class="layui-elem-quote">
                        <b>Access Token请求地址:</b> <span
                                style="color: blue">${createTokenUrl!'<span style="color:red">无可用的网关或鉴权中心, 请假检查网关和鉴权中心是否正常开启</span></@compress>'}</span>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <b>请求方式</b>: <span style="color: blue">POST</span> <br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>参数1</b>: <span
                                style="color: blue">clientId</span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <b>类型</b>: <span style="color: blue">String</span> &nbsp;&nbsp;&nbsp;&nbsp; <b>解释</b>: <span
                                style="color: blue">申请注册的client id</span>
                        <br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>参数2</b>: <span style="color: blue">clientSecret</span>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <b>类型</b>:
                        <span style="color: blue">String</span> &nbsp;&nbsp;&nbsp;&nbsp; <b>解释</b>: <span
                                style="color: blue">申请注册的client
                            secret</span><br/>
                        <br/>
                        <b>Access Token注销地址</b>: <span
                                style="color: blue">${logoutTokenUrl!'<span style="color:red">无可用的网关或鉴权中心, 请假检查网关和鉴权中心是否正常开启</span></@compress>'}</span>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <b>请求方式:</b> <span style="color: blue">POST</span> <br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>参数1:</b> <span
                                style="color: blue">clientId</span>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <b>类型:</b> <span style="color: blue">String</span> &nbsp;&nbsp;&nbsp;&nbsp; <b>解释:</b> <span
                                style="color: blue">申请注册的client id</span> <br/>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>参数2:</b> <span
                                style="color: blue">accessToken</span> &nbsp;&nbsp;&nbsp;&nbsp; <b>类型:</b>
                        <span style="color: blue">String</span> &nbsp;&nbsp;&nbsp;&nbsp; <b>解释:</b> <span
                                style="color: blue">授权获取的access token</span><br/>
                    </blockquote>
                </div>
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const $ = layui.$, admin = layui.admin;
            $("#btnRefreshAccessToken").click(function () {
                layer.confirm("确定要生成Client Id=[${app.clientId}]的Access Token吗?", function (index) {
                    admin.post("do-create-access-token", {'clientId': '${app.clientId}'}, function (data) {
                            $("#accessToken").html(data.data.accessToken);
                            $("#expire").val(data.data.expiresTime);
                            layer.close(index);
                        }
                    );
                });
            });

            $("#btnRefresh").click(function () {
                admin.post("do-refresh-access-token", {'clientId': '${app.clientId}'}, function (data) {
                        $("#accessToken").html(data.data.accessToken);
                        $("#expire").val(data.data.expiresTime);
                    }
                );
            });

            $("#btnLogoutAccessToken").click(function () {
                layer.confirm("确定要注销Client Id=[${app.clientId}]的Access Token吗?", function (index) {
                    admin.post("do-logout-access-token", {'clientId': '${app.clientId}'}, function (data) {
                            $("#accessToken").html(data.data.accessToken);
                            $("#expire").val(data.data.expiresTime);
                            layer.close(index);
                        }
                    );
                });
            });
        });
    </script>
    </body>
    </html>
</@compress>