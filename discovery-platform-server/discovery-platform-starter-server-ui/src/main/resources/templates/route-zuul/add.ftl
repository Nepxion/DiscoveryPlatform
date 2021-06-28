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
                <select id="portalName" name="portalName" lay-filter="portalName" lay-verify="required" lay-search>
                    <option value="">请选择网关名称</option>
                    <#list gatewayNames as gatewayName>
                        <option value="${gatewayName}">${gatewayName}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">服务名称</label>
            <div class="layui-input-inline" style="width: 740px">
                <select id="serviceId" name="serviceId" lay-filter="serviceId" autocomplete="off" lay-verify="required"
                        class="layui-select" lay-search>
                    <option value="">请选择服务名称</option>
                    <#list serviceNames as serviceName>
                        <option value="${serviceName}">${serviceName}</option>
                    </#list>
                </select>
            </div>
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
            const form = layui.form, $ = layui.$;

            form.on('select(serviceId)', function (data) {
                const text = data.elem[data.elem.selectedIndex].text;
                $("#path").val('/' + text + '/**');
                form.render();
                $("#description").focus();
            });

            <#if (gatewayNames?size==1) >
            chooseSelectOption('portalName', 1);
            </#if>

            <#if (serviceNames?size==1) >
            chooseSelectOption('serviceId', 1);
            </#if>
        });
    </script>
    </body>
    </html>
</@compress>