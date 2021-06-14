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
            <label class="layui-form-label">网关名称</label>
            <div class="layui-input-inline" style="width: 850px">
                <select id="gatewayName" name="gatewayName" lay-filter="gatewayName" lay-verify="required" lay-search>
                    <option value="">请选择网关名称</option>
                    <#list gatewayNames as gatewayName>
                        <option value="${gatewayName}">${gatewayName}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">描述信息</label>
            <div class="layui-input-inline" style="width: 850px">
                <input type="text" name="description" class="layui-input" placeholder="请输入该${((type!'')=='VERSION')?string('版本','区域    ')}蓝绿的描述信息" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">策略配置</label>
            <div class="layui-input-block">
                <a id="btnStrategyAdd" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加兜底策略</a>
                <a id="btnConditionAdd" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe674;</i>添加条件策略</a>
                <a id="btnRemove" class="layui-btn layui-btn-sm layui-btn-danger"><i class="layui-icon">&#xe640;</i>删除策略</a>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"></label>
            <div class="layui-input-block">
                <div class="layui-tab layui-tab-brief" lay-filter="tab">
                    <ul id="tabTitle" class="layui-tab-title">
                    </ul>
                    <div id="tabContent" class="layui-tab-content">
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">内置参数</label>

            <div class="layui-input-block">
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
                    <option value="">请选择服务</option>
                    <#list serviceNames as serviceName>
                        <option value="${serviceName}" {{ d.serviceName=='${serviceName}' ?'selected="selected"' : '' }}>${serviceName}</option>
                    </#list>
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
                <select name='operator' lay-filter='operator' tag="$_INDEX_$" lay-search>
                    <#list operators as operator>
                        <option value="${operator.value}" {{ d.operator=='${operator.value}' ?'selected="selected"' : '' }}>
                        ${operator.value}
                        </option>
                    </#list>
                </select>
            </script>

            <script type="text/html" id="tLogic$_INDEX_$">
                <select name='logic' lay-filter='logic' tag="$_INDEX_$" lay-search>
                    <option value="&&" {{ d.logic=='&&' ?
                    'selected="selected"' : '' }}>AND</option>
                    <option value="||" {{ d.logic=='||' ?
                    'selected="selected"' : '' }}>OR</option>
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
                <div class="layui-col-md8">
                    <input type="text" id="spelCondition$_INDEX_$" class="layui-input" placeholder="请输入SPEL表达式" autocomplete="off">
                </div>
                <div class="layui-col-md3" style="text-align:center;margin-top: 3px">
                    <div class="layui-btn-group">
                        <button class="layui-btn layui-btn-sm" lay-event="addVersion">
                            <i class="layui-icon">&#xe656;</i>&nbsp;聚合条件
                        </button>
                        <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="addRegion">
                            <i class="layui-icon">&#xe672;</i>&nbsp;校验条件
                        </button>
                    </div>
                </div>
            </div>

            <hr/>
            <span class="layui-badge layui-bg-blue">路由策略</span>

            <table class="layui-hide" id="gridRoute$_INDEX_$" lay-filter="gridRoute$_INDEX_$"></table>

            <script type="text/html" id="tServiceName$_INDEX_$">
                <select name='serviceName' lay-filter='serviceName' tag="$_INDEX_$" lay-search>
                    <option value="">请选择服务</option>
                    <#list serviceNames as serviceName>
                        <option value="${serviceName}" {{ d.serviceName=='${serviceName}' ?'selected="selected"' : '' }}>
                        ${serviceName}
                        </option>
                    </#list>
                </select>
            </script>

            <script type="text/html" id="tValue$_INDEX_$">
                <select name='value' lay-filter='value' tag="$_INDEX_$" lay-search>
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

        <input type="hidden" id="strategy" name="strategy"/>
        <input type="hidden" id="condition" name="condition"/>
        <input type="hidden" id="route" name="route"/>
        <input type="hidden" id="header" name="header"/>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
                const form = layui.form, admin = layui.admin, $ = layui.$, element = layui.element, table = layui.table;
                const TAB = 'tab', TAB_CONDITION = 'tabCondition', TAB_STRATEGY = 'tabStrategy';
                let tabIndex = 0, tabSelect = TAB_STRATEGY, tabSelectTitle = '兜底策略', headerCount = 0, conditionCount = 0, routeCount = 0;

                addTabCondition();
                addTabCondition();
                addStrategy();

                $('#btnStrategyAdd').click(function () {
                    addStrategy();
                });

                $('#btnConditionAdd').click(function () {
                    addTabCondition();
                });

                $('#btnRemove').click(function () {
                    layer.confirm('确定要删除[' + tabSelectTitle + ']选项卡吗?', function (index) {
                        element.tabDelete(TAB, tabSelect);
                        layer.close(index);
                    });
                });

                element.on('tab(tab)', function () {
                    tabSelect = $(this).attr('lay-id');
                    tabSelectTitle = $(this).html();
                });

                function addTabCondition() {
                    tabIndex++;
                    const tabTitleId = 'tabCondition' + tabIndex;
                    const tabContentId = 'tabContent' + tabIndex;
                    const gridCondition = 'gridCondition' + tabIndex;
                    const gridRoute = 'gridRoute' + tabIndex;
                    $('#tabTitle').append('<li id="' + tabTitleId + '" lay-id="' + tabTitleId + '">条件策略' + tabIndex + '</li>');
                    $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="layui-tab-item"></div>');
                    $('#' + tabContentId).append($('#conditionTemplate').html().replaceAll('$_INDEX_$', tabIndex));
                    element.render(TAB);

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
                            {field: 'parameterName', title: '参数名', edit: 'text'},
                            {title: '运算符', templet: '#tOperator' + tabIndex, width: 100},
                            {field: 'value', title: '值', edit: 'text'},
                            {title: '关系', templet: '#tLogic' + tabIndex, width: 100},
                            {fixed: 'right', title: '操作', align: 'center', toolbar: '#grid-condition-bar', width: 100}
                        ]],
                        data: [newConditionRow()]
                    });

                    table.on('tool(' + gridCondition + ')', function (obj) {
                        const gd = table.cache[gridCondition];
                        if (obj.event === 'addCondition') {
                            gd.push(newConditionRow());
                            reload(gridCondition, gd);
                            reload(gridRoute);
                        } else if (obj.event === 'removeCondition') {
                            if (gd.length > 1) {
                                $.each(gd, function (i, item) {
                                    if (item && item.index == obj.data.index) {
                                        gd.remove(item);
                                    }
                                });
                                reload(gridCondition, gd);
                                reload(gridRoute);
                            }
                        }
                    });

                    form.on('select(operator)', function (obj) {
                        const gridId = 'gridCondition' + $(obj.elem).attr('tag');
                        const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                        const gd = table.cache[gridId];
                        gd[dataIndex]['operator'] = obj.value;
                        reload(gridId, gd);
                    });

                    form.on('select(logic)', function (obj) {
                        const gridId = 'gridCondition' + $(obj.elem).attr('tag');
                        const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                        const gd = table.cache[gridId];
                        gd[dataIndex]['logic'] = obj.value;
                        reload(gridId, gd);
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
                            {type: 'numbers', title: '序号', width: 50},
                            {templet: '#tServiceName' + tabIndex, title: '服务名'},
                            {title: '${((type!'')=='VERSION')?string('版本号','区域值')}', templet: '#tValue' + tabIndex},
                            {fixed: 'right', title: '操作', align: 'center', toolbar: '#grid-route-bar', width: 150}
                        ]],
                        data: [newRouteRow()]
                    });

                    table.on('tool(' + gridRoute + ')', function (obj) {
                        const gd = table.cache[gridRoute];
                        if (obj.event === 'addRoute') {
                            gd.push(newConditionRow());
                            reload(gridCondition);
                            reload(gridRoute, gd);
                        } else if (obj.event === 'removeRoute') {
                            if (gd.length > 1) {
                                $.each(gd, function (i, item) {
                                    if (item && item.index == obj.data.index) {
                                        gd.remove(item);
                                    }
                                });
                                reload(gridCondition);
                                reload(gridRoute, gd);
                            }
                        } else if (obj.event === 'refreshRoute') {

                        }
                    });

                    form.on('select(serviceName)', function (obj) {
                        const gridId = 'gridRoute' + $(obj.elem).attr('tag');
                        const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                        const gd = table.cache[gridId];
                        const serviceName = obj.value;
                        admin.post('do-list-service-metadata', {'serviceName': serviceName}, function (result) {
                            const valueList = [], set = new Set();
                            $.each(result.data, function (index, item) {
                                <#if type=='VERSION'>
                                if (!set.has(item.version)) {
                                    set.add(item.version);
                                    valueList.push(item.version);
                                }
                                <#else>
                                if (!set.has(item.region)) {
                                    set.add(item.region);
                                    valueList.push(item.region);
                                }
                                </#if>
                            });
                            gd[dataIndex]['serviceName'] = serviceName;
                            gd[dataIndex]['valueList'] = valueList;
                            reload(gridId, gd);
                        });
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
                    $('#tabTitle').prepend('<li class="layui-this" lay-id="tabStrategy">兜底策略</li>');
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
                            {type: 'numbers', title: '序号', width: 50},
                            {field: 'serviceName', templet: '#tStrategyServiceName', title: '服务名'},
                            {title: '${((type!'')=='VERSION')?string('版本号','区域值')}', templet: '#tStrategyValue'},
                            {fixed: 'right', title: '操作', align: 'center', toolbar: '#grid-route-bar', width: 150}
                        ]],
                        data: [newRouteRow()]
                    });

                    table.on('tool(gridStrategy)', function (obj) {
                        const gd = table.cache['gridStrategy'];
                        if (obj.event === 'addRoute') {
                            gd.push(newRouteRow());
                            reload('gridStrategy', gd);
                        } else if (obj.event === 'removeRoute') {
                            if (gd.length > 1) {
                                $.each(gd, function (i, item) {
                                    if (item && item.index == obj.data.index) {
                                        gd.remove(item);
                                    }
                                });
                                reload('gridStrategy', gd);
                            }
                        } else if (obj.event === 'refreshRoute') {

                        }
                    });

                    form.on('select(strategyServiceName)', function (obj) {
                        const gridId = 'gridStrategy';
                        const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                        const gd = table.cache[gridId];
                        const serviceName = obj.value;
                        admin.post('do-list-service-metadata', {'serviceName': serviceName}, function (result) {
                            const valueList = [], set = new Set();
                            $.each(result.data, function (index, item) {
                                <#if type=='VERSION'>
                                if (!set.has(item.version)) {
                                    set.add(item.version);
                                    valueList.push(item.version);
                                }
                                <#else>
                                if (!set.has(item.region)) {
                                    set.add(item.region);
                                    valueList.push(item.region);
                                }
                                </#if>
                            });
                            gd[dataIndex]['serviceName'] = serviceName;
                            gd[dataIndex]['valueList'] = valueList;
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

                function newConditionRow() {
                    conditionCount++;
                    return {
                        'index': conditionCount,
                        'parameterName': '',
                        'operator': '',
                        'value': '',
                        'logic': ''
                    };
                }

                function newRouteRow() {
                    routeCount++;
                    return {
                        'index': routeCount,
                        'serviceName': '',
                        'value': ''
                    };
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
                        {type: 'numbers', title: '序号'},
                        {field: 'headerName', title: '请求头', edit: 'text'},
                        {
                            title: '操作符', align: 'center', width: 100, templet: function () {
                                return '=';
                            }
                        },
                        {field: 'value', title: '值', edit: 'text'},
                        {fixed: 'right', title: '操作', align: 'center', toolbar: '#grid-header-bar', width: 100}
                    ]],
                    data: [newHeaderRow()]
                });

                table.on('tool(gridHeader)', function (obj) {
                    const gd = table.cache['gridHeader'];
                    if (obj.event === 'addHeader') {
                        gd.push(newHeaderRow());
                        reload('gridHeader', gd);
                    } else if (obj.event === 'removeHeader') {
                        if (gd.length > 1) {
                            $.each(gd, function (i, item) {
                                if (item && item.index == obj.data.index) {
                                    gd.remove(item);
                                }
                            });
                            reload('gridHeader', gd);
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
                                        'spelCondition': spelCondition
                                    };
                                    const dataStr = JSON.stringify(data);
                                    if (!_setCondition.has(dataStr)) {
                                        _setCondition.add(dataStr);
                                        _dataCondition.push(data);
                                    }
                                }
                            });
                            if (_dataCondition.length > 0) {
                                dataCondition['condition' + tabIndex] = _dataCondition;
                            }
                            const _dataRoute = [], _setRoute = new Set();
                            const gridRoute = 'gridRoute' + tabIndex;
                            $.each(table.cache[gridRoute], function (index, item) {
                                if (item.serviceName != '' && item.value != '') {
                                    const data = {
                                        'serviceName': item.serviceName,
                                        'value': item.value
                                    };
                                    const dataStr = JSON.stringify(data);
                                    if (!_setRoute.has(dataStr)) {
                                        _setRoute.add(dataStr);
                                        _dataRoute.push(data);
                                    }
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
                        }
                    });
                    $('#header').val(JSON.stringify(dataHeader));
                }

                <#if (gatewayNames?size==1) >
                chooseSelectOption('gatewayName', 1);
                </#if>
            }
        );
    </script>
    </body>
    </html>
</@compress>