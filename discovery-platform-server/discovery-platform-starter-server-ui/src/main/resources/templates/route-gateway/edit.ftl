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
            <label class="layui-form-label">网关名称</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" readonly="readonly" id="gatewayName" name="gatewayName" lay-verify="required"
                       class="layui-input layui-disabled" value="${route.gatewayName}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">目标地址</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" id="uri" name="uri" class="layui-input" placeholder="请选择或输入转发的目标地址"
                       style="position:absolute;z-index:2;width:96%;" lay-verify="required" autocomplete="off"
                       value="${route.uri}">
                <select name="uri1" lay-filter="uri1" autocomplete="off" lay-verify="required"
                        class="layui-select" lay-search>
                    <#list serviceNames as serviceName>
                        <option value="lb://${serviceName}" tag="${serviceName}">lb://${serviceName}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">服务名称</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" readonly="readonly" id="serviceName" name="serviceName" lay-verify="required"
                       class="layui-input layui-disabled" value="${route.serviceName}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">
                <a href="https://cloud.spring.io/spring-cloud-gateway/reference/html/" target="_blank" title="帮助文档">
                    断言器
                </a>
            </label>
            <div class="layui-input-inline" style="width: 740px;margin-top:-20px">
                <div class="layui-tab layui-tab-brief">
                    <ul class="layui-tab-title">
                        <li class="layui-this">内置</li>
                        <li>自定义</li>
                    </ul>
                    <div class="layui-tab-content">
                        <div class="layui-tab-item layui-show">
                             <textarea id="predicates" name="predicates" class="layui-input" autocomplete="off"
                                       placeholder='请输入断言字符串（使用换行分隔）。例如：&#13;Path=/routeA/**,/routeB/**&#13;Header=type,animal&#13;Cookie=color,blue'
                                       style="height:75px;resize: none">${route.predicates}</textarea>
                        </div>
                        <div class="layui-tab-item">
                             <textarea id="userPredicates" name="userPredicates" class="layui-input" autocomplete="off"
                                       placeholder='请输入自定义断言字符串（使用换行分隔）。例如：&#13;MyPredicate={"key":"value"}'
                                       style="height:75px;resize: none">${route.userPredicates}</textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">
                <a href="https://cloud.spring.io/spring-cloud-gateway/reference/html/" target="_blank" title="帮助文档">
                    过滤器
                </a>
            </label>
            <div class="layui-input-inline" style="width: 740px;margin-top:-40px">
                <div class="layui-tab layui-tab-brief">
                    <ul class="layui-tab-title">
                        <li class="layui-this">内置</li>
                        <li>自定义</li>
                    </ul>
                    <div class="layui-tab-content">
                        <div class="layui-tab-item layui-show">
                                  <textarea id="filters" name="filters" class="layui-input" autocomplete="off"
                                            placeholder="请输入过滤字符串（使用换行分隔）。例如：&#13;StripPrefix=1&#13;PrefixPath=/routeA&#13;RewritePath=/routeB"
                                            style="height:75px;resize: none">${route.filters}</textarea>
                        </div>
                        <div class="layui-tab-item">
                                   <textarea id="userFilters" name="userFilters" class="layui-input" autocomplete="off"
                                             placeholder='请输入自定义过滤字符串（使用换行分隔）。例如：&#13;MyFilter={"key":"value"}'
                                             style="height:75px;resize: none">${route.userFilters}</textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-form-item" style="margin-top: -100px">
            <label class="layui-form-label">元数据</label>
            <div class="layui-input-inline" style="width: 740px">
                <textarea id="metadata" name="metadata" class="layui-input" autocomplete="off"
                          placeholder="请输入元数据字符串（使用换行分隔）。例如：&#13;foo=bar&#13;food=chocolate"
                          style="height:75px;resize: none">${route.metadata}</textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">执行顺序</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="number" name="order" lay-verify="required" class="layui-input"
                       placeholder="请输入顺序号" autocomplete="off" value="${route.order}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否启用</label>
            <div class="layui-input-block">
                <input type="radio" name="enableFlag" value="true" title="启用" ${(route.enableFlag)?string('checked','')}>
                <input type="radio" name="enableFlag" value="false" title="禁用" ${(route.enableFlag)?string('','checked')}>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">路由描述</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" id="description" name="description" class="layui-input"
                       placeholder="请输入该条路由的描述信息" autocomplete="off" value="${route.description}">
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const form = layui.form, $ = layui.$;

            form.on('select(uri1)', function (data) {
                const text = data.elem[data.elem.selectedIndex].text;
                const name = $(data.elem[data.elem.selectedIndex]).attr('tag');
                $("#uri").val(text);
                $("#serviceName").val(name);
                $("#predicates").val('Path=/' + name + '/**');
                $("#filters").val('StripPrefix=1');
                $("#uri1").next().find("dl").css({"display": "none"});
                form.render();
                $("#description").select();
            });

            $('#uri').bind('input propertychange', function () {
                const value = $("#uri").val();
                $("#uri1").val(value);
                form.render();
                $("#uri1").next().find("dl").css({"display": "block"});
                const dl = $("#uri1").next().find("dl").children();
                let j = -1;
                for (let i = 0; i < dl.length; i++) {
                    if (dl[i].innerHTML.indexOf(value) <= -1) {
                        dl[i].style.display = "none";
                        j++;
                    }
                    if (j == dl.length - 1) {
                        $("#uri1").next().find("dl").css({"display": "none"});
                    }
                }
            });
        });
    </script>
    </body>
    </html>
</@compress>