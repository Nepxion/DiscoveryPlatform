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
            <label class="layui-form-label">目标地址</label>
            <div class="layui-input-inline" style="width: 660px">
                <input type="text" id="uri" name="uri" lay-filter="uri" class="layui-input" placeholder="请选择或输入转发的目标地址" style="position:absolute;z-index:2;width:96%;" lay-verify="required" autocomplete="off">
                <select id="uri1" name="uri1" lay-filter="uri1" autocomplete="off" lay-verify="required" class="layui-select" lay-search>
                </select>
            </div>
            <a id="btnRefreshService" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                <i class="layui-icon">&#xe669;</i>
            </a>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">服务名称</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" id="serviceName" name="serviceName" lay-verify="required" class="layui-input"
                       placeholder="请输入服务名称（对应为注册中心中该服务的名称）" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">
                断言器&nbsp;<a href="https://cloud.spring.io/spring-cloud-gateway/reference/html/" target="_blank" title="帮助文档"><i class="layui-icon layui-icon-about"></i></a>
            </label>
            <div class="layui-input-inline" style="width: 740px;margin-top:-20px">
                <div class="layui-tab layui-tab-brief">
                    <ul class="layui-tab-title">
                        <li class="layui-this" style="color: black">内置</li>
                        <li style="color: black">自定义</li>
                    </ul>
                    <div class="layui-tab-content">
                        <div class="layui-tab-item layui-show">
                             <textarea id="predicates" name="predicates" class="layui-input" autocomplete="off"
                                       placeholder='请输入断言字符串（使用换行分隔）。例如：&#13;Path=/routeA/**,/routeB/**&#13;Header=type,animal&#13;Cookie=color,blue'
                                       style="height:75px;resize: none"></textarea>
                        </div>
                        <div class="layui-tab-item">
                             <textarea id="userPredicates" name="userPredicates" class="layui-input" autocomplete="off"
                                       placeholder='请输入自定义断言字符串（使用换行分隔）。例如：&#13;MyPredicate={"key":"value"}'
                                       style="height:75px;resize: none"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">
                过滤器&nbsp;<a href="https://cloud.spring.io/spring-cloud-gateway/reference/html/" target="_blank" title="帮助文档"><i class="layui-icon layui-icon-about"></i></a>
            </label>
            <div class="layui-input-inline" style="width: 740px;margin-top:-40px">
                <div class="layui-tab layui-tab-brief">
                    <ul class="layui-tab-title">
                        <li class="layui-this" style="color: black">内置</li>
                        <li style="color: black">自定义</li>
                    </ul>
                    <div class="layui-tab-content">
                        <div class="layui-tab-item layui-show">
                                  <textarea id="filters" name="filters" class="layui-input" autocomplete="off"
                                            placeholder="请输入过滤字符串（使用换行分隔）。例如：&#13;StripPrefix=1&#13;PrefixPath=/routeA&#13;RewritePath=/routeB"
                                            style="height:75px;resize: none"></textarea>
                        </div>
                        <div class="layui-tab-item">
                                   <textarea id="userFilters" name="userFilters" class="layui-input" autocomplete="off"
                                             placeholder='请输入自定义过滤字符串（使用换行分隔）。例如：&#13;MyFilter={"key":"value"}'
                                             style="height:75px;resize: none"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-form-item" style="margin-top: -100px">
            <label class="layui-form-label">元数据</label>
            <div class="layui-input-inline" style="width: 740px">
                <textarea id="metadata" name="metadata" class="layui-input" autocomplete="off"
                          placeholder="请输入元数据字符串（使用换行分隔）。例如：&#13;connect-timeout=45000&#13;response-timeout=45s"
                          style="height:75px;resize: none"></textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">执行顺序</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="number" name="order" lay-verify="required" class="layui-input"
                       placeholder="请输入顺序号" autocomplete="off" value="0">
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

            form.on('select(uri1)', function (data) {
                serviceName = data.value;
                const text = data.elem[data.elem.selectedIndex].text;
                const name = $(data.elem[data.elem.selectedIndex]).attr('tag');
                $("#uri").val(text);
                $("#serviceName").val(name);
                $("#predicates").val('Path=/' + name + '/**');
                $("#filters").val('StripPrefix=1');
                $("#uri1").next().find("dl").css({"display": "none"});
                form.render();
                $("#description").focus();
            });

            $("#uri").on("input", function (e) {
                serviceName = e.delegateTarget.value;
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
                const serviceNames = admin.getServiceName();
                const selServiceName = $("#uri1");
                $('#uri').val('');
                $("#serviceName").val('');
                $("#predicates").val('');
                $("#filters").val('');
                selServiceName.html('<option value="">请选择或输入转发的目标地址</option>');
                let chooseIndex = 1;
                $.each(serviceNames, function (i, v) {
                    let option;
                    const value = 'lb://' + v;
                    if (serviceName == undefined && i == 0) {
                        option = $("<option>").attr('selected', 'selected').attr('tag', v).val(value).text(value);
                        chooseIndex = i + 1;
                    } else if (serviceName == value) {
                        option = $("<option>").attr('selected', 'selected').attr('tag', v).val(value).text(value);
                        chooseIndex = i + 1;
                    } else {
                        option = $("<option>").attr('tag', v).val(value).text(value);
                    }
                    selServiceName.append(option);
                });
                layui.form.render('select');
                chooseSelectOption('uri1', chooseIndex);
            }
        });
    </script>
    </body>
    </html>
</@compress>