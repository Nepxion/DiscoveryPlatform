<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
    </head>
    <body>

    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin" style="padding: 20px 30px 0 0;">
        <div class="layui-form-item">
            <label class="layui-form-label">网关名称</label>
            <div class="layui-input-inline" style="width: 740px">
                <div>
                    <div class="layui-input-inline" style="width: 660px">
                        <select id="portalName" name="portalName" lay-filter="portalName" lay-verify="required" lay-search>
                        </select>
                    </div>
                    <a id="btnRefreshPortal" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                        <i class="layui-icon">&#xe669;</i>
                    </a>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">服务名称</label>
            <div class="layui-input-inline" style="width: 660px">
                <select id="serviceId" name="serviceId" lay-filter="serviceId" autocomplete="off" lay-verify="required" class="layui-select" lay-search>
                </select>
            </div>
            <a id="btnRefreshService" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                <i class="layui-icon">&#xe669;</i>
            </a>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">匹配路径</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" id="path" name="path" lay-verify="required" class="layui-input" placeholder="请输入匹配路径" lay-verify="required" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">转发路径</label>
            <div class="layui-input-inline">
                <input type="text" name="url" class="layui-input" style="width: 740px" placeholder="请输入转发地址" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否过滤</label>
            <div class="layui-input-block">
                <input type="radio" name="stripPrefix" value="true" title="是" checked>
                <input type="radio" name="stripPrefix" value="false" title="否">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否重试</label>
            <div class="layui-input-block">
                <input type="radio" name="retryable" value="true" title="是">
                <input type="radio" name="retryable" value="false" title="否" checked>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">过滤敏感头</label>
            <div class="layui-input-block" style="width: 740px">
                <textarea id="sensitiveHeaders" name="sensitiveHeaders" class="layui-input" autocomplete="off" placeholder="请输入敏感请求头（使用逗号分隔）。 例如：&#13;Cookie,Set-Cookie,Authorization" style="height:100px;resize: none"></textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否自定义敏感头</label>
            <div class="layui-input-block">
                <input type="radio" name="customSensitiveHeaders" value="true" title="启用">
                <input type="radio" name="customSensitiveHeaders" value="false" title="禁用" checked>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否启用</label>
            <div class="layui-input-block">
                <input type="radio" name="enableFlag" value="true" title="启用" checked>
                <input type="radio" name="enableFlag" value="false" title="禁用">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">路由描述</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" id="description" name="description" class="layui-input"
                       placeholder="请输入该条路由的描述信息" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const form = layui.form, $ = layui.$, admin = layui.admin;
            let portalName = undefined, serviceName = undefined;

            setTimeout(function () {
                reloadPortalName();
                reloadServiceName();
            }, 50);

            form.on('select(portalName)', function (data) {
                portalName = data.value;
            });

            form.on('select(serviceId)', function (data) {
                serviceName = data.value;
                const text = data.elem[data.elem.selectedIndex].text;
                $("#path").val('/' + text + '/**');
                form.render();
                $("#description").focus();
            });

            $('#btnRefreshPortal').click(function () {
                reloadPortalName();
            });

            $('#btnRefreshService').click(function () {
                reloadServiceName();
            });

            function reloadPortalName() {
                admin.post('do-list-portal-names', {}, function (result) {
                    const selPortalName = $("select[name=portalName]");
                    selPortalName.html('<option value="">请选择网关名称</option>');
                    let index = 0;
                    $.each(result.data, function (key, val) {
                        let option;
                        if (portalName == undefined && index == 0) {
                            option = $('<option>').attr('selected', 'selected').val(val).text(val);
                        } else if (portalName == val) {
                            option = $('<option>').attr('selected', 'selected').val(val).text(val);
                        } else {
                            option = $('<option>').val(val).text(val);
                        }
                        selPortalName.append(option);
                        index++;
                    });
                    layui.form.render('select');
                });
            }

            function reloadServiceName() {
                admin.post('do-list-service-names', {}, function (result) {
                    const selServiceId = $("select[name=serviceId]");
                    selServiceId.html('<option value="">请选择服务</option>');
                    let index = 0, chooseIndex = 1;
                    $.each(result.data, function (key, val) {
                        let option;
                        if (serviceName == undefined && index == 0) {
                            option = $("<option>").attr('selected', 'selected').val(val).text(val);
                            chooseIndex = index + 1;
                        } else if (serviceName == val) {
                            option = $("<option>").attr('selected', 'selected').val(val).text(val);
                            chooseIndex = index + 1;
                        } else {
                            option = $("<option>").val(val).text(val);
                        }
                        selServiceId.append(option);
                        index++;
                    });
                    layui.form.render('select');
                    chooseSelectOption('serviceId', chooseIndex);
                });
            }
        });
    </script>
    </body>
    </html>
</@compress>