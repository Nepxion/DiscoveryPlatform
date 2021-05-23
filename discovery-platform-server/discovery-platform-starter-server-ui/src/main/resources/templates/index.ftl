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
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:void(0)" style="cursor: default">
                            <span>当前版本:&nbsp;${version}</span>
                        </a>
                    </li>
                </ul>
                <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">
                    <li class="layui-nav-item layui-hide-xs" lay-unselect>
                        <a href="javascript:" layadmin-event="fullscreen">
                            <i class="layui-icon layui-icon-screen-full"></i>
                        </a>
                    </li>
                    <li class="layui-nav-item" style="margin-right: 10px" lay-unselect>
                        <a href="javascript:">
                            <cite>${admin.name}</cite>
                        </a>
                        <dl class="layui-nav-child">
                            <dd><a lay-href="${ctx}/info">基本资料</a></dd>
                            <dd><a lay-href="${ctx}/password">修改密码</a></dd>
                            <hr>
                            <dd layadmin-event="logout" style="text-align: center;"><a>退出</a></dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="layui-side layui-side-menu">
                <div class="layui-side-scroll">
                    <div class="layui-logo" lay-href="${ctx}/dashboard/index">
                        <span>${shortName}</span>
                    </div>
                    <ul class="layui-nav layui-nav-tree" lay-shrink="all" id="LAY-system-side-menu"
                        lay-filter="layadmin-system-side-menu">
                        <#if admin.permissions??>
                            <#list admin.permissions as page>
                                <li data-name="config" class="layui-nav-item <#if page_index==0>layui-nav-itemed</#if>">
                                    <#if page.isBlank>
                                        <a href="${ctx}${page.url}" target="_blank" lay-direction="2"
                                           lay-tips="<#if page.remark!=''>${page.remark}<#else>${page.name}</#if>">
                                            <i class="layui-icon ${page.iconClass}"></i><cite>${page.name}</cite>
                                        </a>
                                    <#else>
                                        <a href="javascript:"
                                                <#if page.url!=''> lay-href="${ctx}${page.url}"</#if>
                                           lay-tips="<#if page.remark!=''>${page.remark}<#else>${page.name}</#if>"
                                           lay-direction="2"
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
                                                    <a lay-href="${ctx}${child.url}" target="_blank">${child.name}</a>
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
        layui.config({base: '..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
        });
    </script>
    </body>
    </html>
</@compress>