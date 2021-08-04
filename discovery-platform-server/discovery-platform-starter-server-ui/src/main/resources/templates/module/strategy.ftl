<#include "condition.ftl">
<#include "rate.ftl">
<#macro strategy>
    <#assign isUpdate=(entity.portalName!'')!=''>
    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin" style="padding: 20px 30px 0 0;">
        <div class="layui-form-item">
            <#if !isUpdate>
                <div class="layui-form-item">
                    <label class="layui-form-label">入口类型</label>
                    <div class="layui-input-block">
                        <input type="radio" lay-filter="portalType" name="portalType" value="1" title="网关" ${((entity.portalType!'1')==1) ? string('checked','')}>
                        <input type="radio" lay-filter="portalType" name="portalType" value="2" title="服务" ${((entity.portalType!'1')==2) ? string('checked','')}>
                        <input type="radio" lay-filter="portalType" name="portalType" value="3" title="组" ${((entity.portalType!'1')==3) ? string('checked','')}>
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
            <#else>
                <div class="layui-form-item">
                    <label class="layui-form-label">入口类型</label>
                    <div class="layui-input-inline" style="width: 850px">
                        <input type="text" readonly="readonly" class="layui-input layui-disabled" value="<#if entity.portalType==1>网关<#elseif entity.portalType==2>服务<#elseif entity.portalType==3>组</#if>">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label" style="margin-top: 15px">入口名称</label>
                    <div class="layui-input-inline" style="width: 850px;margin-top: 15px">
                        <input type="text" name="portalName" class="layui-input layui-disabled" readonly="readonly" value="${entity.portalName!''}">
                    </div>
                </div>
            </#if>

            <div class="layui-form-item">
                <label class="layui-form-label" style="margin-top: 15px">描述信息</label>
                <div class="layui-input-inline" style="width: 850px;margin-top: 15px">
                    <input type="text" name="description" class="layui-input" placeholder="请输入描述信息" autocomplete="off" value="${entity.description!''}">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">策略配置</label>
                <div class="layui-input-block">
                    <div class="layui-btn-group">
                        <a id="btnAddBasicBlueGreen" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加蓝绿兜底策略</a>
                        <a id="btnAddBlueGreen" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加蓝绿条件策略</a>
                    </div>

                    <div class="layui-btn-group">
                        <a id="btnAddBasicGray" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加灰度兜底策略</a>
                        <a id="btnAddGray" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加灰度条件策略</a>
                    </div>

                    <div class="layui-btn-group">
                        <a id="btnRemoveStrategy" class="layui-btn layui-btn-sm layui-btn-danger"><i class="layui-icon">&#xe640;</i>删除策略</a>
                    </div>
                </div>
            </div>

            <div class="layui-form-item" style="margin-top:-25px">
                <label class="layui-form-label"></label>
                <div class="layui-input-block" style="width: 850px">
                    <div class="layui-tab layui-tab-brief" lay-filter="tab">
                        <ul id="tabTitle" class="layui-tab-title">
                        </ul>
                        <div id="tabContent" class="layui-tab-content">
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

            <div id="basicBlueGreenTemplate" style="display: none">
                <span class="layui-badge layui-bg-blue">链路选取</span>

                <div class="layui-row" style="margin-top: 10px;">
                    <div class="layui-col-md11">
                        <select id="basicBlueGreenRouteId" name="basicBlueGreenRouteId" autocomplete="off" class="layui-select" lay-search>
                        </select>
                    </div>
                    <div class="layui-col-md1">
                        <a id="btnRefreshBlueGreenRouteId" tag="basicBlueGreenRouteId" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                            <i class="layui-icon">&#xe669;</i>
                        </a>
                    </div>
                </div>
            </div>
            <div id="blueGreenTemplate" style="display: none">
                <@condition gridId="gridBlueGreen"/>

                <span class="layui-badge layui-bg-blue" style="margin-top:15px;">链路选取</span>

                <div class="layui-row" style="margin-top: 10px;">
                    <div class="layui-col-md11">
                        <select id="routeId$_INDEX_$" autocomplete="off" class="layui-select" lay-search>
                        </select>
                    </div>
                    <div class="layui-col-md1">
                        <a id="btnReloadBlueGreenRoute$_INDEX_$" tag="routeId$_INDEX_$" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                            <i class="layui-icon">&#xe669;</i>
                        </a>
                    </div>
                </div>
            </div>

            <div id="basicGrayTemplate" style="display: none">
                <@rate gridId="gridBasicGrayRate"/>
            </div>

            <div id="grayTemplate" style="display: none">
                <@condition gridId="gridGray"/>
                <br/>
                <@rate gridId="gridGrayRate"/>
            </div>

            <input type="hidden" id="id" name="id" value="${entity.id!''}"/>
            <input type="hidden" id="error" name="error" value=""/>
            <input type="hidden" id="strategyType" name="strategyType" value="${strategyType}"/>
            <input type="hidden" id="routeIds" name="routeIds" value=""/>
            <input type="hidden" id="basicBlueGreenStrategyRouteId" name="basicBlueGreenStrategyRouteId" value=""/>
            <input type="hidden" id="blueGreenStrategy" name="blueGreenStrategy" value=""/>
            <input type="hidden" id="basicGrayStrategy" name="basicGrayStrategy" value=""/>
            <input type="hidden" id="grayStrategy" name="grayStrategy" value=""/>
            <input type="hidden" id="header" name="header" value=""/>
        </div>
        <script>
            layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
                    const form = layui.form, admin = layui.admin, $ = layui.$, element = layui.element, table = layui.table;
                    const TAB = 'tab', TAB_STRATEGY_BLUE_GREEN = 'tabStrategyBlueGreen', TAB_STRATEGY_GRAY = 'tabStrategyGray';
                    const TAB_STRATEGY_BASIC_BLUE_GREEN = TAB_STRATEGY_BLUE_GREEN + 'BasicBlueGreen';
                    const TAB_STRATEGY_BASIC_GRAY = TAB_STRATEGY_GRAY + 'Gray';
                    let routeIds = [];
                    let portalType = 1, tabIndex = 0, tabSelectTitle = '', tabSelect, headerCount = 0;
                    let blueGreenCount = 0, basicGrayCount = 0, grayCount = 0;
                    const blueGreenStrategy = ${((entity.blueGreenStrategy!'')?length>0)?string((entity.blueGreenStrategy!''),'{}')};
                    const basicBlueGreenStrategyRouteId = '${((entity.basicBlueGreenStrategyRouteId!'')?length>0)?string((entity.basicBlueGreenStrategyRouteId!''),'')}';
                    const header = ${((entity.header!'')?length>0)?string((entity.header!''),'[]')};
                    setTimeout(function () {
                        <#if !isUpdate>
                        $('#btnRefreshPortal').click();
                        </#if>
                        $('#btnRefreshRouteId').click();
                        <#if !isUpdate>
                        addTabBasicBlueGreen();
                        addTabBlueGreen();
                        addTabBlueGreen();
                        element.tabChange(TAB, TAB_STRATEGY_BASIC_BLUE_GREEN);
                        <#else>
                        let hasBasicBlueGreen = false;
                        if (basicBlueGreenStrategyRouteId != '') {
                            addTabBasicBlueGreen(basicBlueGreenStrategyRouteId);
                            hasBasicBlueGreen = true;
                        }

                        for (const k in blueGreenStrategy) {
                            const condition = blueGreenStrategy[k].condition;
                            const routeId = blueGreenStrategy[k].routeId;
                            addTabBlueGreen(condition, routeId);
                        }
                        if (hasBasicBlueGreen)
                            element.tabChange(TAB, TAB_STRATEGY_BASIC_BLUE_GREEN);
                        else
                            element.tabChange(TAB, TAB_STRATEGY_BLUE_GREEN + 1);
                        </#if>

                    }, 100);

                    form.on('radio(portalType)', function (opt) {
                        portalType = opt.value;
                        $('#btnRefreshPortal').click();
                    });

                    element.on('tab(tab)', function () {
                        tabSelect = $(this).attr('lay-id');
                        tabSelectTitle = $(this).html();
                    });

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
                        data: newHeaderRow(header)
                    });

                    table.on('tool(gridHeader)', function (obj) {
                        const gd = table.cache['gridHeader'];
                        if (obj.event === 'addHeader') {
                            gd.push(newHeaderRow()[0]);
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

                    $('#btnRefreshPortal').click(function () {
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
                    });

                    $('#btnAddBasicBlueGreen').click(function () {
                        addTabBasicBlueGreen();
                        element.tabChange(TAB, TAB_STRATEGY_BASIC_BLUE_GREEN);
                    });

                    $('#btnAddBlueGreen').click(function () {
                        addTabBlueGreen();
                        element.tabChange(TAB, TAB_STRATEGY_BLUE_GREEN + tabIndex);
                    });

                    $('#btnAddBasicGray').click(function () {
                        addTabBasicGray();
                        element.tabChange(TAB, TAB_STRATEGY_BASIC_GRAY);
                    });

                    $('#btnAddGray').click(function () {
                        addTabGray();
                        element.tabChange(TAB, TAB_STRATEGY_GRAY + tabIndex);
                    });

                    $('#btnRemoveStrategy').click(function () {
                        if ($('#tabContent > div').size() < 1) {
                            admin.error("系统提示", "删除策略失败, 条件策略已空");
                            return;
                        }
                        layer.confirm('确定要删除 [' + tabSelectTitle + '] 吗?', function (index) {
                            element.tabDelete(TAB, tabSelect);
                            layer.close(index);
                        });
                    });

                    function existBasicBlueGreen() {
                        return $('li[lay-id="' + TAB_STRATEGY_BASIC_BLUE_GREEN + '"]').size() > 0;
                    }

                    function existBasicGray() {
                        return $('li[lay-id="' + TAB_STRATEGY_BASIC_GRAY + '"]').size() > 0;
                    }

                    function addTabBasicBlueGreen(data) {
                        if (existBasicBlueGreen()) {
                            element.tabChange(TAB, TAB_STRATEGY_BASIC_BLUE_GREEN);
                            admin.success('系统操作', '已存在蓝绿兜底策略');
                            return;
                        }
                        const tabTitleId = TAB_STRATEGY_BASIC_BLUE_GREEN, tabContentId = 'tabContentBasicBlueGreen';
                        $('#tabTitle').append('<li id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span><img width="22" height="22" src="${ctx}/images/graph/service_yellow_64.png">&nbsp;蓝绿兜底</span></li>');
                        $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#basicBlueGreenTemplate').html());
                        element.render(TAB);
                        $('#btnRefreshBlueGreenRouteId').click(function () {
                            bindRouteSelect($(this).attr('tag'), data);
                        });
                        $('#btnRefreshBlueGreenRouteId').click();
                    }

                    function addTabBlueGreen(condition, routeId) {
                        tabIndex++;
                        const tabTitleId = TAB_STRATEGY_BLUE_GREEN + tabIndex, tabContentId = 'tabContent' + tabIndex, gridBlueGreen = 'gridBlueGreen' + tabIndex, btnReloadBlueGreenRoute = 'btnReloadBlueGreenRoute' + tabIndex;
                        $('#tabTitle').append('<li id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span><img width="22" height="22" src="${ctx}/images/graph/service_blue_green_64.png">&nbsp;蓝绿条件<b>' + tabIndex + '</b></span></li>');
                        $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#blueGreenTemplate').html().replaceAll('$_INDEX_$', tabIndex));
                        element.render(TAB);
                        initConditionGrid('gridBlueGreen', tabIndex, condition);
                        $('#' + btnReloadBlueGreenRoute).click(function () {
                            bindRouteSelect($(this).attr('tag'), routeId);
                        }).click();
                    }

                    function addTabBasicGray(data) {
                        if (existBasicGray()) {
                            element.tabChange(TAB, TAB_STRATEGY_BASIC_GRAY);
                            admin.success('系统操作', '已存在灰度兜底策略');
                            return;
                        }
                        const tabTitleId = TAB_STRATEGY_BASIC_GRAY, tabContentId = 'tabContentBasicGray';
                        $('#tabTitle').append('<li id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span><img width="22" height="22" src="${ctx}/images/graph/service_yellow_64.png">&nbsp;灰度兜底</span></li>');
                        $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#basicGrayTemplate').html().replaceAll('$_INDEX_$', ''));
                        element.render(TAB);
                        initRateGrid('gridBasicGrayRate', '');
                    }

                    function addTabGray(condition) {
                        tabIndex++;
                        const tabTitleId = TAB_STRATEGY_GRAY + tabIndex, tabContentId = 'tabContent' + tabIndex;
                        $('#tabTitle').append('<li id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span><img width="22" height="22" src="${ctx}/images/graph/service_black_64.png">&nbsp;灰度条件<b>' + tabIndex + '</b></span></li>');
                        $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#grayTemplate').html().replaceAll('$_INDEX_$', tabIndex));
                        element.render(TAB);
                        initConditionGrid('gridGray', tabIndex, condition);
                        initRateGrid('gridGrayRate', tabIndex);
                    }

                    function bindRouteSelect(id, data) {
                        admin.loading(function () {
                            let basicStrategyRouteId = '${entity.basicStrategyRouteId!''}';
                            if (data) basicStrategyRouteId = data;
                            const routeNames = admin.getRoutes(${strategyType});
                            const sel = $('#' + id);
                            sel.html('<option value="">请选择链路名称</option>');
                            $.each(routeNames, function (key, val) {
                                let option;
                                if (basicStrategyRouteId == val) {
                                    option = $("<option>").attr('selected', 'selected').val(val).text(val);
                                } else {
                                    option = $("<option>").val(val).text(val);
                                }
                                sel.append(option);
                            });
                            layui.form.render('select');
                        });
                    }

                    function newConditionRow(data) {
                        if (data) {
                            const result = [];
                            for (let i = 0; i < data.length; i++) {
                                blueGreenCount++;
                                return [{
                                    'index': blueGreenCount,
                                    'parameterName': data[i].parameterName,
                                    'operator': data[i].operator,
                                    'value': data[i].value,
                                    'logic': data[i].logic
                                }];
                            }
                            if (result.length < 1) {
                                blueGreenCount++;
                                return [{
                                    'index': blueGreenCount,
                                    'parameterName': '',
                                    'operator': '==',
                                    'value': '',
                                    'logic': 'and'
                                }];
                            }
                            return result;
                        } else {
                            blueGreenCount++;
                            return [{
                                'index': blueGreenCount,
                                'parameterName': '',
                                'operator': '==',
                                'value': '',
                                'logic': 'and'
                            }];
                        }
                    }

                    function newHeaderRow(data) {
                        if (data) {
                            const result = [];
                            for (let i = 0; i < data.length; i++) {
                                headerCount++;
                                return [{
                                    'index': headerCount,
                                    'headerName': data[i].headerName,
                                    'value': data[i].value
                                }];
                            }
                            if (result.length < 1) {
                                headerCount++;
                                return [{
                                    'index': headerCount,
                                    'headerName': '',
                                    'value': ''
                                }];
                            }
                            return result;
                        } else {
                            headerCount++;
                            return [{
                                'index': headerCount,
                                'headerName': '',
                                'value': ''
                            }];
                        }
                    }

                    function newRateRow() {
                        basicGrayCount++;
                        const routeNames = admin.getRoutes(${strategyType});
                        return [{
                            'index': basicGrayCount,
                            'routeId': '',
                            'rate': '',
                            'routeIdList': routeNames
                        }];
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

                    function initConditionGrid(prefixGridId, tabIndex, defaultCondition) {
                        const gridId = prefixGridId + tabIndex;
                        $('#btnAssemble' + tabIndex).click(function () {
                            const index = $(this).attr('tag');
                            const spelConditionId = 'spelCondition' + index;
                            let spelExpress = '';

                            const gd = table.cache[gridId];
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
                                title: '<i class="layui-icon layui-icon-ok-circle" style="color: #1E9FFF;"></i>&nbsp;校验条件',
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
                            elem: '#' + gridId,
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
                            data: newConditionRow(defaultCondition)
                        });
                        table.on('tool(' + gridId + ')', function (obj) {
                            const gd = table.cache[gridId];
                            if (obj.event === 'addCondition') {
                                gd.push(newConditionRow()[0]);
                                reload(gridId, gd);
                                $('div[class="layui-table-mend"]').remove();
                            } else if (obj.event === 'removeCondition') {
                                if (gd.length > 1) {
                                    $.each(gd, function (i, item) {
                                        if (item && item.index == obj.data.index) {
                                            gd.remove(item);
                                        }
                                    });
                                    reload(gridId, gd);
                                    $('div[class="layui-table-mend"]').remove();
                                    const tabIndex = $(obj.tr).find('select[name="operator"]').attr('tag');
                                    $('#btnAssemble' + tabIndex).click();
                                }
                            }
                        });
                        table.on('edit(' + gridId + ')', function (obj) {
                            const tabIndex = $(obj.tr).find('select[name="operator"]').attr('tag');
                            $('#btnAssemble' + tabIndex).click();
                        });
                        form.on('select(operator)', function (obj) {
                            const tabIndex = $(obj.elem).attr('tag');
                            const gridId = prefixGridId + tabIndex;
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            gd[dataIndex]['operator'] = obj.value;
                            reload(gridId, gd);
                            $('#btnAssemble' + tabIndex).click();
                        });
                        form.on('select(logic)', function (obj) {
                            const tabIndex = $(obj.elem).attr('tag');
                            const gridId = prefixGridId + tabIndex;
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            gd[dataIndex]['logic'] = obj.value;
                            reload(gridId, gd);
                            $('#btnAssemble' + tabIndex).click();
                        });
                        $('#btnAssemble' + tabIndex).click();
                    }

                    function initRateGrid(prefixGridId, tabIndex) {
                        const gridId = prefixGridId + tabIndex;
                        table.render({
                            elem: '#' + gridId,
                            cellMinWidth: 80,
                            page: false,
                            limit: 99999999,
                            limits: [99999999],
                            even: false,
                            loading: false,
                            cols: [[
                                {type: 'numbers', title: '序号', unresize: true, width: 50},
                                {field: 'routeId', templet: '#templateRouteId' + tabIndex, unresize: true, title: '路由名'},
                                {field: 'rate', title: '流量配比 [输入0 ~ 100的数字]', edit: 'text', unresize: true},
                                {title: '操作', align: 'center', toolbar: '#grid-route-bar', unresize: true, width: 150}
                            ]],
                            data: newRateRow()
                        });
                        table.on('tool(' + gridId + ')', function (obj) {
                            const gd = table.cache[gridId];
                            if (obj.event === 'addRoute') {
                                gd.push(newRateRow()[0]);
                                reload(gridId, gd);
                                $('div[class="layui-table-mend"]').remove();
                            } else if (obj.event === 'removeRoute') {
                                if (gd.length > 1) {
                                    $.each(gd, function (i, item) {
                                        if (item && item.index == obj.data.index) {
                                            gd.remove(item);
                                        }
                                    });
                                    reload(gridId, gd);
                                    $('div[class="layui-table-mend"]').remove();
                                }
                            } else if (obj.event === 'refreshRoute') {
                                admin.loading(function () {
                                    $.each(gd, function (index, item) {
                                        if (item.index == obj.data.index) {
                                            item['routeIdList'] = admin.getRoutes(${strategyType});
                                            return;
                                        }
                                    });
                                    reload(gridId, gd);
                                });
                            }
                        });
                        form.on('select(grayRouteId' + tabIndex + ')', function (obj) {
                            const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                            const gd = table.cache[gridId];
                            gd[dataIndex]['routeId'] = obj.value;
                            reload(gridId, gd);
                        });
                    }

                    $('#callback').click(function () {
                        routeIds = [];
                        collectBasicBlueGreenStrategy();
                        collectBlueGreenStrategy();
                        collectBasicGrayStrategy();
                        collectGrayStrategy();
                        collectHeader();
                        $('#routeIds').val(JSON.stringify(admin.distinct(routeIds)));
                    });

                    function collectBasicBlueGreenStrategy() {
                        $('#basicBlueGreenStrategyRouteId').val('');
                        if (existBasicBlueGreen()) {
                            $('#basicBlueGreenStrategyRouteId').val($('#basicBlueGreenRouteId').val());
                            routeIds.push($('#basicBlueGreenStrategyRouteId').val());
                        }
                    }

                    function collectBlueGreenStrategy() {
                        $('#blueGreenStrategy').val('');
                        const all = {};
                        $('#tabContent').find('.layui-tab-item').each(function () {
                            const tabIndex = $(this).attr('tag');
                            if (tabIndex) {
                                const _dataCondition = [], _setCondition = new Set();
                                const gridBlueGreen = 'gridBlueGreen' + tabIndex;
                                const spelCondition = $(this).find('#spelCondition' + tabIndex).val();
                                $.each(table.cache[gridBlueGreen], function (index, item) {
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
                                        $('#error').val('蓝绿条件' + tabIndex + '的参数名或值不允许为空');
                                        return false;
                                    }
                                });
                                if ($('#error').val() !== '') {
                                    return false;
                                }
                                const routeId = $('#routeId' + tabIndex).val();

                                if (routeId) {
                                    all['cr' + tabIndex] = {
                                        'condition': _dataCondition,
                                        'routeId': routeId
                                    };
                                    routeIds.push(routeId);
                                }
                            }
                        });
                        $('#blueGreenStrategy').val(JSON.stringify(all));
                    }

                    function collectBasicGrayStrategy() {
                        $('#basicGrayStrategy').val('');
                        const all = [], set = new Set();
                        $.each(table.cache['gridBasicGrayRate'], function (index, item) {
                            const json = {'routeId': item.routeId, 'rate': item.rate}, key = JSON.stringify(json);
                            if (!set.has(key)) {
                                set.add(key);
                                all.push(json);
                            }
                        });
                        $('#basicGrayStrategy').val(JSON.stringify(all));
                    }

                    function collectGrayStrategy() {
                        $('#grayStrategy').val('');
                        const all = {};
                        $('#tabContent').find('.layui-tab-item').each(function () {
                            const tabIndex = $(this).attr('tag');
                            if (tabIndex) {
                                const _dataCondition = [], _setCondition = new Set();
                                const gridGray = 'gridGray' + tabIndex, gridGrayRate = 'gridGrayRate' + tabIndex;
                                const spelCondition = $(this).find('#spelCondition' + tabIndex).val();
                                $.each(table.cache[gridGray], function (index, item) {
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
                                        $('#error').val('灰度条件' + tabIndex + '的参数名或值不允许为空');
                                        return false;
                                    }
                                });
                                if ($('#error').val() !== '') {
                                    return false;
                                }
                                const _rates = [], _setRates = new Set();
                                $.each(table.cache[gridGrayRate], function (index, item) {
                                    const json = {'routeId': item.routeId, 'rate': item.rate}, key = JSON.stringify(json);
                                    routeIds.push(item.routeId);
                                    if (!_setRates.has(key)) {
                                        _setRates.add(key);
                                        _rates.push(json);
                                    }
                                });
                                if (_dataCondition.length > 0 || _rates.length > 0) {
                                    all['cr' + tabIndex] = {
                                        'condition': _dataCondition,
                                        'rate': _rates
                                    };
                                }
                            }
                        });
                        $('#grayStrategy').val(JSON.stringify(all));
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
                }
            );
        </script>
    </div>
</#macro>