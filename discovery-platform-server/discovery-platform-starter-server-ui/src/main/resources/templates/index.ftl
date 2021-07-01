<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "common/layui.ftl">
    </head>
    <body class="layui-layout-body">
    <div id="LAY_app">
        <div class="layui-layout layui-layout-admin">
            <div class="layui-header">
                <ul class="layui-nav layui-layout-left">
                    <li class="layui-nav-item layadmin-flexible" lay-unselect>
                        <a href="javascript:" layadmin-event="flexible" title="侧边伸缩">
                            <i class="layui-icon layui-icon-shrink-right" id="LAY_app_flexible"></i>
                        </a>
                    </li>
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:" layadmin-event="refresh" title="刷新">
                            <i class="layui-icon layui-icon-refresh-3"></i>
                        </a>
                    </li>
                    <li class="layui-nav-item" style="width: 40px" lay-unselect>
                        <a href="javascript:" title="文档">
                            <i class="layui-icon layui-icon-link"></i>
                        </a>
                        <dl class="layui-nav-child" style="width: 160px">
                            <dd><a href="http://nepxion.com/discovery" target="_blank">&nbsp;&nbsp;&nbsp;<img width="24" height="24" src="${ctx}/images/Discovery32.png" alt="Discovery">&nbsp;&nbsp;&nbsp;Discovery&nbsp;文档</a></dd>
                            <dd><a href="http://nepxion.com/polaris" target="_blank">&nbsp;&nbsp;&nbsp;<img width="24" height="24" src="${ctx}/images/Polaris32.png" alt="Discovery">&nbsp;&nbsp;&nbsp;Polaris&nbsp;文档</a></dd>
                        </dl>
                    </li>
                </ul>
                <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">
                    <li class="layui-nav-item layui-hide-xs" lay-unselect>
                        <a href="javascript:" layadmin-event="fullscreen">
                            <i class="layui-icon layui-icon-screen-full"></i>
                        </a>
                    </li>
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:" title="当前使用的注册中心">
                            <i class="layui-icon layui-icon-component"></i>
                            <cite id="discovery-type"></cite>
                        </a>
                    </li>
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:" title="当前使用的配置中心">
                            <i class="layui-icon layui-icon-note"></i>
                            <cite id="config-type"></cite>
                        </a>
                    </li>
                    <li class="layui-nav-item" style="text-align: center;margin-right: 10px" lay-unselect>
                        <a href="javascript:">
                            <cite>${admin.name}</cite>
                        </a>
                        <dl class="layui-nav-child">
                            <dd><a lay-href="${ctx}/info">基本资料</a></dd>
                            <dd><a lay-href="${ctx}/password">修改密码</a></dd>
                            <hr>
                            <dd layadmin-event="logout" style=""><a>退出</a></dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="layui-side layui-side-menu">
                <div class="layui-side-scroll">
                    <div class="layui-logo" lay-href="${ctx}/dashboard/index">
                        <img width="30px" height="30px" src="${ctx}/images/Logo.png">
                        <span>${shortName}</span>
                    </div>
                    <ul class="layui-nav layui-nav-tree" lay-shrink="all" id="LAY-system-side-menu"
                        lay-filter="layadmin-system-side-menu">
                        <#if admin.permissions??>
                            <#list admin.permissions as page>
                                <li data-name="config" class="layui-nav-item <#if page_index==0>layui-nav-itemed</#if>">
                                    <#if page.blankFlag>
                                        <a href="${ctx}${page.url}" target="_blank" lay-direction="2" lay-tips="${page.description}">
                                            <i class="layui-icon ${page.iconClass}"></i><cite>${page.name}</cite>
                                        </a>
                                    <#else>
                                        <a href="javascript:"
                                                <#if page.url!=''> lay-href="${ctx}${page.url}" </#if>
                                           lay-tips="${page.description}" lay-direction="2"
                                                <#if page.url==(admin.defaultPage)!'_blank'> class="layui-this"</#if>
                                        >
                                            <i class="layui-icon ${page.iconClass}"></i><cite>${page.name}</cite>
                                        </a>
                                    </#if>

                                    <#list page.children as child>
                                        <dl class="layui-nav-child">
                                            <#if child.children?? && child.children?size gt 0>
                                                <dd class="layui-nav-itemed">
                                                    <a href="javascript:">${child.name}</a>
                                                    <dl class="layui-nav-child">
                                                        <#list child.children as gs>
                                                            <dd><a lay-href="${ctx}${gs.url}">${gs.name}</a></dd>
                                                        </#list>
                                                    </dl>
                                                </dd>
                                            <#else>
                                                <dd>
                                                    <#if child.blankFlag>
                                                        <a href="${ctx}${child.url}" target="_blank">${child.name}</a>
                                                    <#else>
                                                        <a lay-href="${ctx}${child.url}" target="_blank">${child.name}</a>
                                                    </#if>
                                                </dd>
                                            </#if>
                                        </dl>
                                    </#list>
                                </li>
                            </#list>
                        </#if>
                    </ul>
                </div>
            </div>
            <div class="layadmin-pagetabs" id="LAY_app_tabs">
                <div class="layui-icon layadmin-tabs-control layui-icon-prev" layadmin-event="leftPage"></div>
                <div class="layui-icon layadmin-tabs-control layui-icon-next" layadmin-event="rightPage"></div>
                <div class="layui-icon layadmin-tabs-control layui-icon-down">
                    <ul class="layui-nav layadmin-tabs-select" lay-filter="layadmin-pagetabs-nav">
                        <li class="layui-nav-item" lay-unselect>
                            <a href="javascript:"></a>
                            <dl class="layui-nav-child layui-anim-fadein">
                                <dd layadmin-event="closeThisTabs"><a href="javascript:">关闭当前标签页</a></dd>
                                <dd layadmin-event="closeOtherTabs"><a href="javascript:">关闭其它标签页</a></dd>
                                <dd layadmin-event="closeAllTabs"><a href="javascript:">关闭全部标签页</a></dd>
                            </dl>
                        </li>
                    </ul>
                </div>
                <div class="layui-tab" lay-unauto lay-allowClose="true" lay-filter="layadmin-layout-tabs">
                    <ul class="layui-tab-title" id="LAY_app_tabsheader">
                        <li lay-id="${ctx}${admin.defaultPage!''}" lay-attr="${ctx}${admin.defaultPage!''}"
                            class="layui-this"><i
                                    class="layui-icon layui-icon-home"></i></li>
                    </ul>
                </div>
            </div>
            <div class="layui-body" id="LAY_app_body">
                <div class="layadmin-tabsbody-item layui-show">
                    <iframe src="${ctx}${admin.defaultPage!''}" style="border:0" class="layadmin-iframe"></iframe>
                </div>
            </div>
            <div class="layadmin-body-shade" layadmin-event="shade"></div>
        </div>
    </div>
    <script>
        layui.config({base: '..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table', 'common'], function () {
            const admin = layui.admin, $ = layui.$, form = layui.form, table = layui.table, layer = layui.layer;

            layer.ready(function () {
                const thatAdmin = layui.admin;
                getDiscoveryType(thatAdmin);
                getConfigType(thatAdmin);
            });

            function getDiscoveryType(thatAdmin) {
                thatAdmin.get('/console/discovery-type', function (response) {
                    $("#discovery-type").text(response.data);
                }, function(response) {
                    const msg = 'Get discovery type failed, Error: ' + response.error;
                    admin.error(admin.SYSTEM_PROMPT, msg);
                }, true);
            }

            function getConfigType(thatAdmin) {
                thatAdmin.get('/console/config-type', function (response) {
                    $("#config-type").text(response.data);
                }, function(response) {
                    const msg = 'Get config type failed, Error: ' + response.error;
                    admin.error(admin.SYSTEM_PROMPT, msg);
                }, true);
            }
        });
    </script>
    </body>
    </html>
</@compress>