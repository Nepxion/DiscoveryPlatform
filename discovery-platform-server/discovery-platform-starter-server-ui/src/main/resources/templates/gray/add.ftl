<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
        <link rel="stylesheet" href="${ctx}/css/layui-table-select.css" media="all">
    </head>
    <body>

    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin"
         style="padding: 20px 30px 0 0;">
        <div class="layui-form-item">
            <div class="layui-form-item">
                <label class="layui-form-label">入口类型</label>
                <div class="layui-input-block">
                    <input type="radio" lay-filter="portalType" name="portalType" value="1" title="网关" checked>
                    <input type="radio" lay-filter="portalType" name="portalType" value="2" title="服务">
                    <input type="radio" lay-filter="portalType" name="portalType" value="3" title="组">
                </div>
            </div>

            <label class="layui-form-label">入口名称</label>
            <div class="layui-input-inline" style="width: 850px">
                <div class="layui-row">
                    <div class="layui-col-md11">
                        <select id="portalName" name="portalName" lay-filter="portalName" lay-verify="required" lay-search>
                        </select>
                    </div>
                    <div class="layui-col-md1">
                        <a id="btnRefreshPortal" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                            <i class="layui-icon">&#xe669;</i>
                        </a>
                    </div>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label" style="margin-top: 15px">描述信息</label>
                <div class="layui-input-inline" style="width: 850px;margin-top: 15px">
                    <input type="text" name="description" class="layui-input" placeholder="请输入该${((type!'')=='VERSION')?string('版本','区域')}灰度的描述信息" autocomplete="off">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">策略配置</label>
                <div class="layui-input-block">
                    <a id="btnStrategyAdd" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加兜底策略</a>
                    <a id="btnConditionAdd" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加灰度策略</a>
                    <a id="btnStrategyRemove" class="layui-btn layui-btn-sm layui-btn-danger"><i class="layui-icon">&#xe640;</i>删除策略</a>
                </div>
            </div>

            <div class="layui-form-item" style="margin-top:-25px">
                <label class="layui-form-label"></label>
                <div class="layui-input-block" style="width: 850px">
                    <div class="layui-tab layui-tab-card" lay-filter="tab">
                        <ul id="tabTitle" class="layui-tab-title">
                        </ul>
                        <div id="tabContent" class="layui-tab-content">
                        </div>
                    </div>
                </div>
            </div>

            <div id="divRouteService">
                <div class="layui-form-item">
                    <label class="layui-form-label">路由编排</label>
                    <div class="layui-input-block">
                        <a id="btnRouteAdd" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加路由</a>
                        <a id="btnRouteRemove" class="layui-btn layui-btn-sm layui-btn-danger"><i class="layui-icon">&#xe640;</i>删除路由</a>
                    </div>
                </div>

                <div class="layui-form-item" style="margin-top:-25px">
                    <label class="layui-form-label"></label>
                    <div class="layui-input-block" style="width: 850px">
                        <div class="layui-tab layui-tab-brief" lay-filter="tabRoute">
                            <ul id="tabRouteTitle" class="layui-tab-title">
                            </ul>
                            <div id="tabRouteContent" class="layui-tab-content">
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="layui-form-item" style="margin-top:-25px">
                <label class="layui-form-label">内置参数</label>
                <div class="layui-input-block" style="width: 850px">
                    <table class="layui-hide" id="gridHeader" lay-filter="gridHeader"></table>

                    <script type="text/html" id="grid-header-bar">
                        <@update>
                            <div class="layui-btn-group">
                                <a class="layui-btn layui-btn-sm" lay-event="addHeader">
                                    <i class="layui-icon">&#xe654;</i></a>
                                <a class="layui-btn layui-btn-warm layui-btn-sm" lay-event="removeHeader">
                                    <i class="layui-icon">&#xe67e;</i></a>
                            </div>
                        </@update>
                    </script>
                </div>
            </div>

            <div class="layui-form-item layui-hide">
                <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
            </div>

            <input id="callback" type="button" style="display: none"/>

            <div id="strategyTemplate" style="display: none">
                <table class="layui-hide" id="gridStrategy" lay-filter="gridStrategy"></table>

                <script type="text/html" id="tStrategyServiceName">
                    <select name='strategyServiceName' lay-filter='strategyServiceName' lay-search>
                        <option value="">请选择服务名称</option>
                        {{# layui.each(d.serviceNameList, function(index, item){ }}
                        <option value="{{ item }}" {{ d.serviceName==item ?
                        'selected="selected"' : '' }}>
                        {{ item }}
                        </option>
                        {{# }); }}
                    </select>
                </script>

                <script type="text/html" id="tStrategyValue">
                    <select name='strategyValue' lay-filter='strategyValue' lay-search>
                        <option value="">请选择${((type!'')=='VERSION')?string('版本号','区域值')}</option>
                        {{# layui.each(d.valueList, function(index, item){ }}
                        <option value="{{ item }}" {{ d.value==item ?
                        'selected="selected"' : '' }}>
                        {{ item }}
                        </option>
                        {{# }); }}
                    </select>
                </script>

                <script type="text/html" id="grid-route-bar">
                    <@update>
                        <div class="layui-btn-group">
                            <a class="layui-btn layui-btn-sm" lay-event="refreshRoute">
                                <i class="layui-icon">&#xe669;</i>
                            </a>
                            <a class="layui-btn layui-btn-sm" lay-event="addRoute">
                                <i class="layui-icon">&#xe654;</i>
                            </a>
                            <a class="layui-btn layui-btn-warm layui-btn-sm" lay-event="removeRoute">
                                <i class="layui-icon">&#xe67e;</i>
                            </a>
                        </div>
                    </@update>
                </script>
            </div>

            <div id="conditionTemplate" style="display: none">
                <span class="layui-badge layui-bg-blue">条件策略</span>
                <table class="layui-hide" id="gridCondition$_INDEX_$" lay-filter="gridCondition$_INDEX_$"></table>

                <script type="text/html" id="tOperator$_INDEX_$">
                    <select name='operator' lay-filter='operator' tag="$_INDEX_$">
                        <#list operators as operator>
                            <option value="${operator.value}" {{ d.operator=='${operator.value}' ?'selected="selected"' : '' }}>
                            ${operator.value}
                            </option>
                        </#list>
                    </select>
                </script>

                <script type="text/html" id="tLogic$_INDEX_$">
                    <select name='logic' lay-filter='logic' tag="$_INDEX_$">
                        <#list logics as logic>
                            <option value="${logic.value}" {{ d.logic=='${logic.value}' ?'selected="selected"' : '' }}>
                            ${logic.value}
                            </option>
                        </#list>
                    </select>
                </script>

                <script type="text/html" id="grid-condition-bar">
                    <@update>
                        <div class="layui-btn-group">
                            <a class="layui-btn layui-btn-sm" lay-event="addCondition">
                                <i class="layui-icon">&#xe654;</i></a>
                            <a class="layui-btn layui-btn-warm layui-btn-sm" lay-event="removeCondition">
                                <i class="layui-icon">&#xe67e;</i></a>
                        </div>
                    </@update>
                </script>

                <div class="layui-row">
                    <div class="layui-col-md9">
                        <input type="text" id="spelCondition$_INDEX_$" class="layui-input" placeholder="聚合条件表达式或者自定义条件表达式" autocomplete="off">
                    </div>
                    <div class="layui-col-md3" style="text-align:center;margin-top: 3px;">
                        <div class="layui-btn-group">
                            <button class="layui-btn layui-btn-sm" id="btnAssemble$_INDEX_$" tag="$_INDEX_$">
                                <i class="layui-icon">&#xe674;</i>&nbsp;聚合条件
                            </button>
                            <button class="layui-btn layui-btn-sm layui-btn-normal" id="btnVerify$_INDEX_$" tag="$_INDEX_$">
                                <i class="layui-icon">&#x1005;</i>&nbsp;校验条件
                            </button>
                        </div>
                    </div>
                </div>

                <span class="layui-badge layui-bg-blue" style="margin-top:15px;">流量配比</span>

                <table class="layui-hide" id="gridRoute$_INDEX_$" lay-filter="gridRoute$_INDEX_$"></table>

                <script type="text/html" id="tRouteName$_INDEX_$">
                    <select name='routeName' lay-filter='routeName' tag="$_INDEX_$" lay-search>
                        <option value="">请选择路由名称</option>
                        {{# layui.each(d.routeNameList, function(index, item){ }}
                        <option value="{{ item }}" {{ d.routeName==item ?
                        'selected="selected"' : '' }}>
                        {{ item }}
                        </option>
                        {{# }); }}
                    </select>
                </script>
            </div>

            <div id="routeTemplate" style="display: none">
                <table class="layui-hide" id="gridRouteService$_INDEX_$" lay-filter="gridRouteService$_INDEX_$"></table>

                <script type="text/html" id="tRouteServiceName$_INDEX_$">
                    <select name='routeServiceName' lay-filter='routeServiceName' tag="$_INDEX_$" lay-search>
                        <option value="">请选择服务名称</option>
                        {{# layui.each(d.serviceNameList, function(index, item){ }}
                        <option value="{{ item }}" {{ d.serviceName==item ?
                        'selected="selected"' : '' }}>
                        {{ item }}
                        </option>
                        {{# }); }}
                    </select>
                </script>

                <script type="text/html" id="tRouteValue$_INDEX_$">
                    <select name='routeValue' lay-filter='routeValue' tag="$_INDEX_$" lay-search>
                        <option value="">请选择${((type!'')=='VERSION')?string('版本号','区域值')}</option>
                        {{# layui.each(d.valueList, function(index, item){ }}
                        <option value="{{ item }}" {{ d.value==item ?
                        'selected="selected"' : '' }}>
                        {{ item }}
                        </option>
                        {{# }); }}
                    </select>
                </script>

                <script type="text/html" id="grid-route-bar">
                    <@update>
                        <div class="layui-btn-group">
                            <a class="layui-btn layui-btn-sm" tag="$_INDEX_$" lay-event="refreshRoute">
                                <i class="layui-icon">&#xe669;</i>
                            </a>
                            <a class="layui-btn layui-btn-sm" tag="$_INDEX_$" lay-event="addRoute">
                                <i class="layui-icon">&#xe654;</i>
                            </a>
                            <a class="layui-btn layui-btn-warm layui-btn-sm" tag="$_INDEX_$" lay-event="removeRoute">
                                <i class="layui-icon">&#xe67e;</i>
                            </a>
                        </div>
                    </@update>
                </script>
            </div>

            <input type="hidden" id="strategy" name="strategy"/>
            <input type="hidden" id="error" name="error" value=""/>
            <input type="hidden" id="condition" name="condition"/>
            <input type="hidden" id="route" name="route"/>
            <input type="hidden" id="header" name="header"/>
            <input type="hidden" id="routeService" name="routeService"/>
        </div>
        <script>
            layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
                    const form = layui.form, admin = layui.admin, $ = layui.$, element = layui.element, table = layui.table;
                    const TAB = 'tab', TAB_ROUTE = 'tabRoute', TAB_STRATEGY = 'tabStrategy';
                    let portalType = 1, serviceNameList = [], routeNameList = [], tabIndex = 0, tabRouteIndex = 0, tabSelect = TAB_STRATEGY, tabSelectTitle = '兜底策略', tabSelectRoute = '', tabSelectRouteTitle = '', headerCount = 0, conditionCount = 0, routeCount = 0, rateCount = 0;

                    setTimeout(function () {
                        reloadPortalName();
                        addRouteService();
                        addRouteService();
                        addTabCondition();
                        addStrategy();
                        element.tabChange(TAB_ROUTE, 'tabRouteService1');
                    }, 100);

                    form.on('radio(portalType)', function (opt) {
                        portalType = opt.value;
                        reloadPortalName();
                    });

                    function reloadPortalName() {
                        admin.post('do-list-portal-names', {'portalTypeInt': portalType}, function (result) {
                            const selPortalName = $("select[name=portalName]");
                            let portalTypeName = '';
                            if (portalType == 1) {
                                portalTypeName = '网关';
                            } else if (portalType == 2) {
                                portalTypeName = '服务';
                            } else if (portalType == 3) {
                                portalTypeName = '组';
                            }
                            selPortalName.html('<option value="">请选择' + portalTypeName + '名称</option>');
                            $.each(result.data, function (key, val) {
                                let option = $("<option>").val(val).text(val);
                                selPortalName.append(option);
                            });
                            layui.form.render('select');
                        });
                    }

                    $('#btnStrategyAdd').click(function () {
                        addStrategy();
                    });

                    $('#btnConditionAdd').click(function () {
                        addTabCondition();
                    });

                    $('#btnStrategyRemove').click(function () {
                        layer.confirm('确定要删除 [' + tabSelectTitle + '] 吗?', function (index) {
                            element.tabDelete(TAB, tabSelect);
                            layer.close(index);
                        });
                    });

                    $('#btnRefreshPortal').click(function () {
                        reloadPortalName();
                    });

                    $('#btnRouteAdd').click(function () {
                        addRouteService();
                    });

                    $('#btnRouteRemove').click(function () {
                        layer.confirm('确定要删除 [' + tabSelectRouteTitle + '] 吗?', function (index) {
                            element.tabDelete(TAB_ROUTE, tabSelectRoute);
                            layer.close(index);
                        });
                    });

                    element.on('tab(tab)', function () {
                        tabSelect = $(this).attr('lay-id');
                        tabSelectTitle = $(this).html();
                        if ($(this).attr('tag') == '1') {
                            $('#divRouteService').hide();
                        } else {
                            $('#divRouteService').show();
                        }
                    });

                    element.on('tab(tabRoute)', function () {
                        tabSelectRoute = $(this).attr('lay-id');
                        tabSelectRouteTitle = $(this).html();
                    });

                    function addTabCondition() {
                        tabIndex++;
                        const tabTitleId = 'tabCondition' + tabIndex;
                        const tabContentId = 'tabContent' + tabIndex;
                        const gridCondition = 'gridCondition' + tabIndex;
                        const gridRoute = 'gridRoute' + tabIndex;
                        $('#tabTitle').append('<li id="' + tabTitleId + '" lay-id="' + tabTitleId + '">灰度策略' + tabIndex + '</li>');
                        $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#conditionTemplate').html().replaceAll('$_INDEX_$', tabIndex));
                        element.render(TAB);

                        $('#btnAssemble' + tabIndex).click(function () {
                            const index = $(this).attr('tag');
                            const spelConditionId = 'spelCondition' + index;
                            let spelExpress = '';

                            const gd = table.cache[gridCondition];
                            $.each(gd, function (index, item) {
                                if (item.parameterName != '' && item.operator != '' && item.value != '' && item.logic != '') {
                                    spelExpress = spelExpress + "#H['" + item.parameterName + "'] " + item.operator + " '" + item.value + "' " + (item.logic == undefined ? "" : item.logic) + " ";
                                }
                            });
                            $('#' + spelConditionId).val($.trim(spelExpress.substring(0, spelExpress.length - 4)));
                        });

                        $('#btnVerify' + tabIndex).click(function () {
                            const index = $(this).attr('tag');
                            const spelConditionId = 'spelCondition' + index;
                            layer.open({
                                type: 2,
                                title: '<i class="layui-icon layui-icon-ok-circle"></i>&nbsp;检验条件',
                                content: 'verify?expression=' + escape($('#' + spelConditionId).val()),
                                area: ['645px', '235px'],
                                btn: '关闭',
                                resize: false,
                                yes: function (index, layero) {
                                    const iframeWindow = window['layui-layer-iframe' + index], submitID = 'btn_confirm',
                                        submit = layero.find('iframe').contents().find('#' + submitID);
                                    iframeWindow.layui.form.on('submit(' + submitID + ')', function (data) {
                                        layer.close(index);
                                    });
                                    submit.trigger('click');
                                }
                            });
                        });

                        table.render({
                            elem: '#' + gridCondition,
                            cellMinWidth: 80,
                            page: false,
                            limit: 99999999,
                            limits: [99999999],
                            even: false,
                            loading: false,
                            cols: [[
                                {type: 'numbers', title: '序号', width: 50},
                                {field: 'parameterName', title: '参数名', unresize: true, edit: 'text', width: 242},
                                {title: '运算符', templet: '#tOperator' + tabIndex, unresize: true, width: 100},
                                {field: 'value', title: '值', edit: 'text', unresize: true, width: 242},
                                {title: '关系符', templet: '#tLogic' + tabIndex, unresize: true, width: 100},
                                {title: '操作', align: 'center', toolbar: '#grid-condition-bar', unresize: true, width: 110}
                            ]],
                            data: [newConditionRow()]
                        });

                        table.on('tool(' + gridCondition + ')', function (obj) {
                            const gd = table.cache[gridCondition];
                            if (obj.event === 'addCondition') {
                                gd.push(newConditionRow());
                                reload(gridCondition, gd);
                                $('div[class="layui-table-mend"]').remove();
                            } else if (obj.event === 'removeCondition') {
                                if (gd.length > 1) {
                                    $.each(gd, function (i, item) {
                                        if (item && item.index == obj.data.index) {
                                            gd.remove(item);
                                        }
                                    });
                                    reload(gridCondition, gd);
                                    $('div[class="layui-table-mend"]').remove();
                                    const tabIndex = $(obj.tr).find('select[name="operator"]').attr('tag');
                                    $('#btnAssemble' + tabIndex).click();
                                }
                            }
                        });

                        table.on('edit(' + gridCondition + ')', function (obj) {
                            const tabIndex = $(obj.tr).find('select[name="operator"]').attr('tag');
                            $('#btnAssemble' + tabIndex).click();
                        });

                        form.on('select(operator)', function (obj) {
                            const tabIndex = $(obj.elem).attr('tag');
                            const gridId = 'gridCondition' + tabIndex;
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            gd[dataIndex]['operator'] = obj.value;
                            reload(gridId, gd);
                            $('#btnAssemble' + tabIndex).click();
                        });

                        form.on('select(logic)', function (obj) {
                            const tabIndex = $(obj.elem).attr('tag');
                            const gridId = 'gridCondition' + tabIndex;
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            gd[dataIndex]['logic'] = obj.value;
                            reload(gridId, gd);
                            $('#btnAssemble' + tabIndex).click();
                        });

                        table.render({
                            elem: '#' + gridRoute,
                            cellMinWidth: 80,
                            page: false,
                            limit: 99999999,
                            limits: [99999999],
                            even: false,
                            loading: false,
                            cols: [[
                                {type: 'numbers', title: '序号', unresize: true, width: 50},
                                {templet: '#tRouteName' + tabIndex, title: '路由名', unresize: true, width: 323},
                                {field: 'value', title: '流量配比(0% ~ 100%)', edit: 'text', unresize: true},
                                {title: '操作', align: 'center', toolbar: '#grid-route-bar', unresize: true, width: 150}
                            ]],
                            data: [newRateRow()]
                        });

                        table.on('tool(' + gridRoute + ')', function (obj) {
                            const gd = table.cache[gridRoute];
                            if (obj.event === 'addRoute') {
                                gd.push(newRateRow());
                                reload(gridRoute, gd);
                                $('div[class="layui-table-mend"]').remove();
                            } else if (obj.event === 'removeRoute') {
                                if (gd.length > 1) {
                                    $.each(gd, function (i, item) {
                                        if (item && item.index == obj.data.index) {
                                            gd.remove(item);
                                        }
                                    });
                                    reload(gridRoute, gd);
                                    $('div[class="layui-table-mend"]').remove();
                                }
                            } else if (obj.event === 'refreshRoute') {
                                refreshRouteNames();
                                $.each(gd, function (index, item) {
                                    if (item.index == obj.data.index) {
                                        item['routeNameList'] = routeNameList;
                                        return;
                                    }
                                });
                                reload(gridRoute, gd);
                            }
                        });

                        form.on('select(routeName)', function (obj) {
                            const gridId = 'gridRoute' + $(obj.elem).attr('tag');
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            const routeName = obj.value;
                            gd[dataIndex]['routeName'] = routeName;
                            reload(gridId, gd);
                        });

                        form.on('select(value)', function (obj) {
                            const gridId = 'gridRoute' + $(obj.elem).attr('tag');
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            gd[dataIndex]['value'] = obj.value;
                            reload(gridId, gd);
                        });
                    }

                    function addStrategy() {
                        if ($('li[lay-id="' + TAB_STRATEGY + '"]').size() > 0) {
                            element.tabChange(TAB, TAB_STRATEGY);
                            admin.success('系统操作', '已存在兜底策略');
                            return;
                        }
                        $('#tabTitle').prepend('<li class="layui-this" tag="1" lay-id="tabStrategy">兜底策略</li>');
                        $('#tabContent').prepend('<div id="contentStrategy" class="layui-tab-item layui-show"></div>');
                        $('#contentStrategy').append($('#strategyTemplate').html());
                        element.render(TAB);
                        element.tabChange(TAB, TAB_STRATEGY);
                        table.render({
                            elem: '#gridStrategy',
                            cellMinWidth: 80,
                            page: false,
                            limit: 99999999,
                            limits: [99999999],
                            even: false,
                            loading: false,
                            cols: [[
                                {type: 'numbers', title: '序号', unresize: true, width: 50},
                                {field: 'serviceName', templet: '#tStrategyServiceName', unresize: true, title: '服务名'},
                                {title: '${((type!'')=='VERSION')?string('版本号','区域值')}', templet: '#tStrategyValue', unresize: true},
                                {title: '操作', align: 'center', toolbar: '#grid-route-bar', unresize: true, width: 150}
                            ]],
                            data: [newRouteRow()]
                        });

                        table.on('tool(gridStrategy)', function (obj) {
                            const gd = table.cache['gridStrategy'];
                            if (obj.event === 'addRoute') {
                                gd.push(newRouteRow());
                                reload('gridStrategy', gd);
                                $('div[class="layui-table-mend"]').remove();
                            } else if (obj.event === 'removeRoute') {
                                if (gd.length > 1) {
                                    $.each(gd, function (i, item) {
                                        if (item && item.index == obj.data.index) {
                                            gd.remove(item);
                                        }
                                    });
                                    reload('gridStrategy', gd);
                                    $('div[class="layui-table-mend"]').remove();
                                }
                            } else if (obj.event === 'refreshRoute') {
                                layer.load();
                                let serviceName = '';
                                refreshServiceNames();
                                layer.closeAll('loading');
                                $.each(gd, function (index, item) {
                                    if (item.index == obj.data.index) {
                                        item['serviceNameList'] = serviceNameList;
                                        serviceName = item['serviceName'];
                                        return;
                                    }
                                });
                                refreshServiceValue(serviceName, function (vl) {
                                    $.each(gd, function (index, item) {
                                        if (item.index == obj.data.index) {
                                            item['valueList'] = vl;
                                            return;
                                        }
                                    });
                                    reload('gridStrategy', gd);
                                });
                            }
                        });

                        form.on('select(strategyServiceName)', function (obj) {
                            const gridId = 'gridStrategy';
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            const serviceName = obj.value;
                            refreshServiceValue(serviceName, function (vl) {
                                gd[dataIndex]['serviceName'] = serviceName;
                                gd[dataIndex]['valueList'] = vl;
                                reload(gridId, gd);
                            });
                        });

                        form.on('select(strategyValue)', function (obj) {
                            const gridId = 'gridStrategy';
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            const strategyValue = obj.value;
                            gd[dataIndex]['value'] = strategyValue;
                            reload(gridId, gd);
                        });
                    }

                    function addRouteService() {
                        tabRouteIndex++;
                        const tabTitleId = 'tabRouteService' + tabRouteIndex;
                        const tabContentId = 'tabRouteContent' + tabRouteIndex;
                        const gridRoute = 'gridRouteService' + tabRouteIndex;
                        $('#tabRouteTitle').append('<li class="routeTitleClass" tag="' + tabRouteIndex + '" id="' + tabTitleId + '" lay-id="' + tabTitleId + '">路由' + tabRouteIndex + '</li>');
                        $('#tabRouteContent').append('<div id="' + tabContentId + '" tag="' + tabRouteIndex + '" class="layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#routeTemplate').html().replaceAll('$_INDEX_$', tabRouteIndex));
                        element.render(TAB_ROUTE);
                        element.tabChange(TAB_ROUTE, tabTitleId);
                        table.render({
                            elem: '#' + gridRoute,
                            cellMinWidth: 80,
                            page: false,
                            limit: 99999999,
                            limits: [99999999],
                            even: false,
                            loading: false,
                            cols: [[
                                {type: 'numbers', title: '序号', unresize: true, width: 50},
                                {field: 'serviceName', templet: '#tRouteServiceName' + tabRouteIndex, unresize: true, title: '服务名'},
                                {title: '${((type!'')=='VERSION')?string('版本号','区域值')}', templet: '#tRouteValue' + tabRouteIndex, unresize: true},
                                {title: '操作', align: 'center', toolbar: '#grid-route-bar', unresize: true, width: 150}
                            ]],
                            data: [newRouteRow()]
                        });

                        table.on('tool(gridRouteService' + tabRouteIndex + ')', function (obj) {
                            const gd = table.cache[gridRoute];
                            if (obj.event === 'addRoute') {
                                gd.push(newRouteRow());
                                reload(gridRoute, gd);
                                $('div[class="layui-table-mend"]').remove();
                            } else if (obj.event === 'removeRoute') {
                                if (gd.length > 1) {
                                    $.each(gd, function (i, item) {
                                        if (item && item.index == obj.data.index) {
                                            gd.remove(item);
                                        }
                                    });
                                    reload(gridRoute, gd);
                                    $('div[class="layui-table-mend"]').remove();
                                }
                            } else if (obj.event === 'refreshRoute') {
                                layer.load();
                                let serviceName = '';
                                refreshServiceNames();
                                layer.closeAll('loading');
                                $.each(gd, function (index, item) {
                                    if (item.index == obj.data.index) {
                                        item['serviceNameList'] = serviceNameList;
                                        serviceName = item['serviceName'];
                                        return;
                                    }
                                });
                                refreshServiceValue(serviceName, function (vl) {
                                    $.each(gd, function (index, item) {
                                        if (item.index == obj.data.index) {
                                            item['valueList'] = vl;
                                            return;
                                        }
                                    });
                                    reload(gridRoute, gd);
                                });
                            }
                        });

                        form.on('select(routeServiceName)', function (obj) {
                            const gridId = 'gridRouteService' + $(obj.elem).attr('tag');
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            const serviceName = obj.value;
                            refreshServiceValue(serviceName, function (vl) {
                                gd[dataIndex]['serviceName'] = serviceName;
                                gd[dataIndex]['valueList'] = vl;
                                reload(gridId, gd);
                            });
                        });

                        form.on('select(routeValue)', function (obj) {
                            const gridId = 'gridRouteService' + $(obj.elem).attr('tag');
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            const routeValue = obj.value;
                            gd[dataIndex]['value'] = routeValue;
                            reload(gridId, gd);
                        });
                    }

                    function newConditionRow() {
                        conditionCount++;
                        return {
                            'index': conditionCount,
                            'parameterName': '',
                            'operator': '==',
                            'value': '',
                            'logic': 'and'
                        };
                    }

                    function newRouteRow() {
                        refreshServiceNames();
                        routeCount++;
                        return {
                            'index': routeCount,
                            'serviceName': '',
                            'value': '',
                            'serviceNameList': serviceNameList,
                            'valueList': []
                        };
                    }

                    function newRateRow() {
                        rateCount++;
                        refreshRouteNames();
                        return {
                            'index': rateCount,
                            'routeName': '',
                            'value': '',
                            'routeNameList': routeNameList
                        };
                    }

                    function refreshServiceNames() {
                        admin.postQuiet('do-list-service-names', {}, function (result) {
                            const set = new Set();
                            serviceNameList = [];
                            $.each(result.data, function (index, item) {
                                if (!set.has(item)) {
                                    set.add(item);
                                    serviceNameList.push(item);
                                }
                            });
                        }, null, false);
                    }

                    function refreshRouteNames() {
                        const set = new Set();
                        routeNameList = [];
                        $('.routeTitleClass').each(function (i, item) {
                            const val = $(item).html();
                            if (!set.has(val)) {
                                set.add(val);
                                routeNameList.push(val);
                            }
                        });
                    }

                    function refreshServiceValue(serviceName, callback) {
                        admin.post('do-list-service-metadata', {'serviceName': serviceName}, function (r) {
                            const vl = [], set = new Set();
                            $.each(r.data, function (index, item) {
                                <#if type=='VERSION'>
                                if (!set.has(item.version)) {
                                    set.add(item.version);
                                    vl.push(item.version);
                                }
                                <#else>
                                if (!set.has(item.region)) {
                                    set.add(item.region);
                                    vl.push(item.region);
                                }
                                </#if>
                            });
                            callback(vl);
                        });
                    }

                    table.render({
                        elem: '#gridHeader',
                        cellMinWidth: 80,
                        page: false,
                        limit: 99999999,
                        limits: [99999999],
                        even: false,
                        loading: false,
                        cols: [[
                            {type: 'numbers', title: '序号', unresize: true, width: 50},
                            {field: 'headerName', title: '请求头', unresize: true, edit: 'text'},
                            {
                                title: '操作符', align: 'center', unresize: true, width: 100, templet: function () {
                                    return '=';
                                }
                            },
                            {field: 'value', title: '值', unresize: true, edit: 'text'},
                            {title: '操作', align: 'center', toolbar: '#grid-header-bar', unresize: true, width: 110}
                        ]],
                        data: [newHeaderRow()]
                    });

                    table.on('tool(gridHeader)', function (obj) {
                        const gd = table.cache['gridHeader'];
                        if (obj.event === 'addHeader') {
                            gd.push(newHeaderRow());
                            reload('gridHeader', gd);
                            $('div[class="layui-table-mend"]').remove();
                        } else if (obj.event === 'removeHeader') {
                            if (gd.length > 1) {
                                $.each(gd, function (i, item) {
                                    if (item && item.index == obj.data.index) {
                                        gd.remove(item);
                                    }
                                });
                                reload('gridHeader', gd);
                                $('div[class="layui-table-mend"]').remove();
                            }
                        }
                    });

                    function newHeaderRow() {
                        headerCount++;
                        return {
                            'index': headerCount,
                            'headerName': '',
                            'value': ''
                        };
                    }

                    function reload(gridId, data) {
                        $.each(data, function (i, d) {
                            d.index = i;
                        });
                        if (data) {
                            table.reload(gridId, {'data': data});
                        } else {
                            table.reload(gridId);
                        }
                    }

                    $('#callback').click(function () {
                        collectStrategy();
                        collectCondition();
                        collectHeader();
                        collectRouteService();
                    });

                    function collectStrategy() {
                        $('#strategy').val('');
                        if ($('#contentStrategy').size() > 0) {
                            const dataStrategy = [], set = new Set();
                            $.each(table.cache['gridStrategy'], function (index, item) {
                                if (item.serviceName != '' && item.value != '') {
                                    const data = {
                                        'serviceName': item.serviceName,
                                        'value': item.value
                                    };
                                    const dataStr = JSON.stringify(data);
                                    if (!set.has(dataStr)) {
                                        set.add(dataStr);
                                        dataStrategy.push(data);
                                    }
                                    $('#error').val('');
                                } else if (item.serviceName + item.value != '') {
                                    $('#error').val('兜底策略的服务名或版本号不允许为空');
                                    return false;
                                }
                            });
                            $('#strategy').val(JSON.stringify(dataStrategy));
                        }
                    }

                    function collectCondition() {
                        $('#condition').val('');
                        $('#route').val('');
                        const dataCondition = {}, dataRoute = {};
                        $('#tabContent').find('.layui-tab-item').each(function () {
                            const tabIndex = $(this).attr('tag');
                            if (tabIndex) {
                                const _dataCondition = [], _setCondition = new Set();
                                const gridCondition = 'gridCondition' + tabIndex;
                                const spelCondition = $(this).find('#spelCondition' + tabIndex).val();
                                $.each(table.cache[gridCondition], function (index, item) {
                                    if (item.parameterName != '' && item.value != '') {
                                        const data = {
                                            'parameterName': item.parameterName,
                                            'operator': item.operator,
                                            'value': item.value,
                                            'logic': item.logic,
                                            'spelCondition': spelCondition
                                        };
                                        const dataStr = JSON.stringify(data);
                                        if (!_setCondition.has(dataStr)) {
                                            _setCondition.add(dataStr);
                                            _dataCondition.push(data);
                                        }
                                        $('#error').val('');
                                    } else if (item.parameterName + item.value != '') {
                                        $('#error').val('灰度策略' + tabIndex + '的条件策略的参数名或值不允许为空');
                                        return false;
                                    }
                                });

                                if ($('#error').val() !== '') {
                                    return false;
                                }
                                if (_dataCondition.length > 0) {
                                    dataCondition['condition' + tabIndex] = _dataCondition;
                                }
                                const _dataRoute = [], _setRoute = new Set();
                                const gridRoute = 'gridRoute' + tabIndex;
                                $.each(table.cache[gridRoute], function (index, item) {
                                    if (item.routeName != '' && item.value != '') {
                                        const data = {
                                            'routeName': item.routeName,
                                            'value': item.value
                                        };
                                        const dataStr = JSON.stringify(data);
                                        if (!_setRoute.has(dataStr)) {
                                            _setRoute.add(dataStr);
                                            _dataRoute.push(data);
                                        }
                                        $('#error').val('');
                                    } else if (item.routeName + item.value != '') {
                                        $('#error').val('灰度策略' + tabIndex + '的路由策略的服务名或${((type!'')=='VERSION')?string('版本号','区域值')}不允许为空');
                                        return false;
                                    }
                                });
                                if (_dataRoute.length > 0) {
                                    dataRoute['route' + tabIndex] = _dataRoute;
                                }
                            }
                        });
                        $('#condition').val(JSON.stringify(dataCondition));
                        $('#route').val(JSON.stringify(dataRoute));
                    }

                    function collectHeader() {
                        $('#header').val('');
                        const dataHeader = [], set = new Set();
                        $.each(table.cache['gridHeader'], function (index, item) {
                            if (item.headerName != '' && item.value != '') {
                                const data = {
                                    'headerName': item.headerName,
                                    'value': item.value
                                };
                                const dataStr = JSON.stringify(data);
                                if (!set.has(dataStr)) {
                                    set.add(dataStr);
                                    dataHeader.push(data);
                                }
                                $('#error').val('');
                            } else if (item.headerName + item.value != '') {
                                $('#error').val('内置参数的请求头或值不允许为空');
                                return false;
                            }
                        });
                        $('#header').val(JSON.stringify(dataHeader));
                    }

                    function collectRouteService() {
                        $('#routeService').val('');
                        const routeService = {};
                        $('#tabRouteTitle').find('li.routeTitleClass').each(function (i, it) {
                            const tabIndex = $(this).attr('tag');
                            if (tabIndex) {
                                const _dataCondition = [], _setCondition = new Set();
                                const gridRoutService = 'gridRouteService' + tabIndex;

                                $.each(table.cache[gridRoutService], function (index, item) {
                                    if (item.parameterName != '' && item.value != '') {
                                        const data = {
                                            'serviceName': item.serviceName,
                                            'value': item.value
                                        };
                                        const dataStr = JSON.stringify(data);
                                        if (!_setCondition.has(dataStr)) {
                                            _setCondition.add(dataStr);
                                            _dataCondition.push(data);
                                        }
                                        $('#error').val('');
                                    } else if (item.serviceName + item.value != '') {
                                        $('#error').val('路由' + tabIndex + '的服务名或版本号不允许为空');
                                        return false;
                                    }
                                });

                                routeService[$(it).html()] = _dataCondition;

                                if ($('#error').val() !== '') {
                                    return false;
                                }
                            }
                        });
                        $('#routeService').val(JSON.stringify(routeService));
                    }
                }
            );
        </script>
    </body>
    </html>
</@compress>