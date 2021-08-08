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
                <div class="layui-input-inline" style="width: 900px">
                    <div class="layui-row">
                        <div class="layui-col-md11">
                            <select id="portalName" name="portalName" lay-filter="portalName" lay-verify="required" lay-search>
                            </select>
                        </div>
                        <div class="layui-col-md1">
                            <button id="btnRefreshPortal" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                                <i class="layui-icon">&#xe669;</i>
                            </button>
                        </div>
                    </div>
                </div>
            <#else>
                <div class="layui-form-item">
                    <label class="layui-form-label">入口类型</label>
                    <div class="layui-input-inline" style="width: 900px">
                        <input type="text" readonly="readonly" class="layui-input layui-disabled" value="<#if entity.portalType==1>网关<#elseif entity.portalType==2>服务<#elseif entity.portalType==3>组</#if>">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label" style="margin-top: 15px">入口名称</label>
                    <div class="layui-input-inline" style="width: 900px;margin-top: 15px">
                        <input type="text" name="portalName" class="layui-input layui-disabled" readonly="readonly" value="${entity.portalName!''}">
                    </div>
                </div>
            </#if>

            <div class="layui-form-item">
                <label class="layui-form-label" style="margin-top: 15px">描述信息</label>
                <div class="layui-input-inline" style="width: 900px;margin-top: 15px">
                    <input type="text" name="description" class="layui-input" placeholder="请输入描述信息" autocomplete="off" value="${entity.description!''}">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">策略配置&nbsp;<a href="http://nepxion.gitee.io/discovery/docs/discovery-doc/Strategy.jpg" target="_blank" title="蓝绿灰度混合发布执行逻辑"><i class="layui-icon layui-icon-about"></i></a></label>
                <div class="layui-input-block">
                    <div class="layui-btn-group">
                        <button id="btnAddBasicGlobal" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加全局兜底策略</button>
                    </div>

                    <div class="layui-btn-group">
                        <button id="btnAddBasicBlueGreen" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加蓝绿兜底策略</button>
                        <button id="btnAddBlueGreen" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加蓝绿策略</button>
                    </div>

                    <div class="layui-btn-group">
                        <button id="btnAddBasicGray" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加灰度兜底策略</button>
                        <button id="btnAddGray" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加灰度策略</button>
                    </div>

                    <div class="layui-btn-group">
                        <button id="btnRemoveStrategy" class="layui-btn layui-btn-sm layui-btn-danger"><i class="layui-icon">&#xe640;</i>删除策略</button>
                    </div>
                </div>
            </div>

            <div class="layui-form-item" style="margin-top:-25px">
                <label class="layui-form-label"></label>
                <div class="layui-input-block" style="width: 900px">
                    <div class="layui-tab layui-tab-brief" lay-filter="tab">
                        <ul id="tabTitle" class="layui-tab-title">
                        </ul>
                        <div id="tabContent" class="layui-tab-content" style="float: left">
                        </div>
                    </div>
                </div>
            </div>

            <div class="layui-form-item" style="margin-top:-25px">
                <label class="layui-form-label">内置参数</label>
                <div class="layui-input-block" style="width: 900px">
                    <table class="layui-hide" id="gridHeader" lay-filter="gridHeader"></table>

                    <script type="text/html" id="grid-header-bar">
                        <@update>
                            <div class="layui-btn-group">
                                <button class="layui-btn layui-btn-sm" lay-event="addHeader">
                                    <i class="layui-icon">&#xe654;</i></button>
                                <button class="layui-btn layui-btn-warm layui-btn-sm" lay-event="removeHeader">
                                    <i class="layui-icon">&#xe67e;</i></button>
                            </div>
                        </@update>
                    </script>
                </div>
            </div>

            <div class="layui-form-item layui-hide">
                <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
            </div>

            <input id="callback" type="button" style="display: none"/>

            <div id="basicGlobalTemplate" style="display: none;width: 900px">
                <span class="layui-badge layui-bg-blue">链路选取</span>

                <div class="layui-row" style="margin-top: 10px;margin-bottom: 10px;">
                    <div class="layui-col-md11">
                        <select id="basicGlobalRouteId" name="basicGlobalRouteId" lay-verify="required" class="layui-select" lay-search>
                        </select>
                    </div>
                    <div class="layui-col-md1">
                        <button id="btnRefreshGlobalRouteId" tag="basicGlobalRouteId" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                            <i class="layui-icon">&#xe669;</i>
                        </button>
                    </div>
                </div>
            </div>

            <div id="basicBlueGreenTemplate" style="display: none;width: 900px">
                <span class="layui-badge layui-bg-blue">链路选取</span>

                <div class="layui-row" style="margin-top: 10px;margin-bottom: 10px;">
                    <div class="layui-col-md11">
                        <select id="basicBlueGreenRouteId" name="basicBlueGreenRouteId" lay-verify="required" class="layui-select" lay-search>
                        </select>
                    </div>
                    <div class="layui-col-md1">
                        <button id="btnRefreshBlueGreenRouteId" tag="basicBlueGreenRouteId" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                            <i class="layui-icon">&#xe669;</i>
                        </button>
                    </div>
                </div>
            </div>
            <div id="blueGreenTemplate" style="display: none">
                <@condition gridId="gridBlueGreen"/>

                <span class="layui-badge layui-bg-blue" style="margin-top:15px;">链路选取</span>

                <div class="layui-row" style="margin-top: 10px;margin-bottom: 10px;">
                    <div class="layui-col-md11">
                        <select id="routeId$_INDEX_$" lay-verify="required" class="layui-select" lay-search>
                        </select>
                    </div>
                    <div class="layui-col-md1">
                        <button id="btnReloadBlueGreenRoute$_INDEX_$" tag="routeId$_INDEX_$" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                            <i class="layui-icon">&#xe669;</i>
                        </button>
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
            <input type="hidden" id="basicGlobalStrategyRouteId" name="basicGlobalStrategyRouteId" value=""/>
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
                    const TAB_STRATEGY_BASIC_GLOBAL = TAB_STRATEGY_BLUE_GREEN + 'BasicGlobal';
                    const TAB_STRATEGY_BASIC_BLUE_GREEN = TAB_STRATEGY_BLUE_GREEN + 'BasicBlueGreen';
                    const TAB_STRATEGY_BASIC_GRAY = TAB_STRATEGY_GRAY + 'BasicGray';
                    let routeIds = [], spels = new Set();
                    let portalType = 1, tabIndex = 0, tabSelectTitle = '', tabSelect, headerCount = 0;
                    let conditionCount = 0, rateCount = 0;
                    const basicGrayStrategy = ${((entity.basicGrayStrategy!'')?length>0)?string((entity.basicGrayStrategy!''),'[]')};
                    const grayStrategy = ${((entity.grayStrategy!'')?length>0)?string((entity.grayStrategy!''),'{}')};
                    const blueGreenStrategy = ${((entity.blueGreenStrategy!'')?length>0)?string((entity.blueGreenStrategy!''),'{}')};
                    const basicGlobalStrategyRouteId = '${((entity.basicGlobalStrategyRouteId!'')?length>0)?string((entity.basicGlobalStrategyRouteId!''),'')}';
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
                        let hasBasicGlobal = false, hasBasicBlueGreen = false, hasBasicGray = false;

                        if (!isEmpty(basicGlobalStrategyRouteId)) {
                            addTabBasicGlobal(basicGlobalStrategyRouteId);
                            hasBasicGlobal = true;
                        }

                        if (!isEmpty(basicBlueGreenStrategyRouteId)) {
                            addTabBasicBlueGreen(basicBlueGreenStrategyRouteId);
                            hasBasicBlueGreen = true;
                        }

                        for (const k in blueGreenStrategy) {
                            const condition = blueGreenStrategy[k].condition;
                            const routeId = blueGreenStrategy[k].routeId;
                            addTabBlueGreen(condition, routeId);
                        }

                        if (!isEmpty(basicGrayStrategy)) {
                            addTabBasicGray(basicGrayStrategy);
                            hasBasicGray = true;
                        }

                        for (const k in grayStrategy) {
                            addTabGray(grayStrategy[k].condition, grayStrategy[k].rate);
                        }

                        if (hasBasicGlobal) {
                            element.tabChange(TAB, TAB_STRATEGY_BASIC_GLOBAL);
                        } else if (hasBasicBlueGreen) {
                            element.tabChange(TAB, TAB_STRATEGY_BASIC_BLUE_GREEN);
                        } else if (hasBasicGray) {
                            element.tabChange(TAB, TAB_STRATEGY_BASIC_GRAY);
                        } else {
                            element.tabChange(TAB, $('#tabTitle>li:first').attr('lay-id'));
                        }
                        </#if>
                    }, 100);

                    form.on('radio(portalType)', function (opt) {
                        portalType = opt.value;
                        $('#btnRefreshPortal').click();
                    });

                    element.on('tab(tab)', function (data) {
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

                    $('#btnAddBasicGlobal').click(function () {
                        addTabBasicGlobal();
                        $(".layui-tab-bar").click();
                        element.init();
                        element.tabChange(TAB, TAB_STRATEGY_BASIC_GLOBAL);
                    });

                    $('#btnAddBasicBlueGreen').click(function () {
                        addTabBasicBlueGreen();
                        $(".layui-tab-bar").click();
                        element.init();
                        element.tabChange(TAB, TAB_STRATEGY_BASIC_BLUE_GREEN);
                    });

                    $('#btnAddBlueGreen').click(function () {
                        addTabBlueGreen();
                        $(".layui-tab-bar").click();
                        element.init();
                        element.tabChange(TAB, TAB_STRATEGY_BLUE_GREEN + tabIndex);
                    });

                    $('#btnAddBasicGray').click(function () {
                        addTabBasicGray();
                        $(".layui-tab-bar").click();
                        element.init();
                        element.tabChange(TAB, TAB_STRATEGY_BASIC_GRAY);
                    });

                    $('#btnAddGray').click(function () {
                        addTabGray();
                        $(".layui-tab-bar").click();
                        element.init();
                        element.tabChange(TAB, TAB_STRATEGY_GRAY + tabIndex);
                    });

                    $('#btnRemoveStrategy').click(function () {
                        if ($('#tabContent > div').size() < 1) {
                            admin.error("系统提示", "删除策略失败, 条件策略已空");
                            return;
                        } else if (tabSelectTitle.length == 47 || tabSelectTitle == '') {
                            admin.error("系统提示", "请先选中要删除的选项卡");
                            return;
                        }

                        layer.confirm('确定要删除 [' + tabSelectTitle + '] 吗?', function (index) {
                            element.tabDelete(TAB, tabSelect);
                            layer.close(index);
                            element.tabChange(TAB, tabSelect);
                            if ($('#tabContent').find('.layui-tab-item').size() < 1) {
                                tabSelectTitle = '';
                            }
                        });
                    });

                    function existBasicGlobal() {
                        return $('li[lay-id="' + TAB_STRATEGY_BASIC_GLOBAL + '"]').size() > 0;
                    }

                    function existBasicBlueGreen() {
                        return $('li[lay-id="' + TAB_STRATEGY_BASIC_BLUE_GREEN + '"]').size() > 0;
                    }

                    function existBasicGray() {
                        return $('li[lay-id="' + TAB_STRATEGY_BASIC_GRAY + '"]').size() > 0;
                    }

                    function addTabBasicGlobal(data) {
                        if (existBasicGlobal()) {
                            admin.error('系统操作', '已存在全局兜底策略', function () {
                                element.tabChange(TAB, TAB_STRATEGY_BASIC_GLOBAL);
                            });
                            return;
                        }
                        const tabTitleId = TAB_STRATEGY_BASIC_GLOBAL, tabContentId = 'tabContentBasicGlobal';
                        $('#tabTitle').append('<li style="float:left;width:70px" id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span style="color: black"><img width="18" height="18" src="${ctx}/images/graph/service_yellow_64.png">&nbsp;全局兜底</span></li>');
                        $('#tabContent').append('<div style="width:900px" id="' + tabContentId + '" tag="' + tabIndex + '" class="basicGlobal layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#basicGlobalTemplate').html());
                        element.render(TAB);
                        let isFirst = true;
                        $('#btnRefreshGlobalRouteId').click(function () {
                            const id = $(this).attr('tag');
                            if (isFirst) {
                                bindRouteSelect(id, data);
                                isFirst = false;
                            } else {
                                admin.loading(function () {
                                    bindRouteSelect(id, data);
                                });
                            }
                        }).click();
                    }

                    function addTabBasicBlueGreen(data) {
                        if (existBasicBlueGreen()) {
                            admin.error('系统操作', '已存在蓝绿兜底策略', function () {
                                element.tabChange(TAB, TAB_STRATEGY_BASIC_BLUE_GREEN);
                            });
                            return;
                        }
                        const tabTitleId = TAB_STRATEGY_BASIC_BLUE_GREEN, tabContentId = 'tabContentBasicBlueGreen';
                        $('#tabTitle').append('<li style="float:left;width:70px" id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span style="color: black"><img width="18" height="18" src="${ctx}/images/graph/service_yellow_64.png">&nbsp;蓝绿兜底</span></li>');
                        $('#tabContent').append('<div style="width:900px" id="' + tabContentId + '" tag="' + tabIndex + '" class="basicBlueGreen layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#basicBlueGreenTemplate').html());
                        element.render(TAB);
                        let isFirst = true;
                        $('#btnRefreshBlueGreenRouteId').click(function () {
                            const id = $(this).attr('tag');
                            if (isFirst) {
                                bindRouteSelect(id, data);
                                isFirst = false;
                            } else {
                                admin.loading(function () {
                                    bindRouteSelect(id, data);
                                });
                            }
                        }).click();
                    }

                    function addTabBlueGreen(condition, routeId) {
                        tabIndex++;
                        const tabTitleId = TAB_STRATEGY_BLUE_GREEN + tabIndex, tabContentId = 'tabContent' + tabIndex, gridBlueGreen = 'gridBlueGreen' + tabIndex, btnReloadBlueGreenRoute = 'btnReloadBlueGreenRoute' + tabIndex;
                        $('#tabTitle').append('<li style="float:left;width:70px" id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span style="color: black"><img width="18" height="18" src="${ctx}/images/graph/service_blue_green_64.png">&nbsp;蓝绿<b>' + tabIndex + '</b></span></li>');
                        $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="blueGreen layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#blueGreenTemplate').html().replaceAll('$_INDEX_$', tabIndex));
                        element.render(TAB);
                        initConditionGrid('gridBlueGreen', tabIndex, condition);
                        let isFirst = true;
                        $('#' + btnReloadBlueGreenRoute).click(function () {
                            const id = $(this).attr('tag');
                            if (isFirst) {
                                bindRouteSelect(id, routeId);
                                isFirst = false;
                            } else {
                                admin.loading(function () {
                                    bindRouteSelect(id, routeId);
                                });
                            }
                        }).click();
                    }

                    function addTabBasicGray(data) {
                        if (existBasicGray()) {
                            admin.error('系统操作', '已存在灰度兜底策略', function () {
                                element.tabChange(TAB, TAB_STRATEGY_BASIC_GRAY);
                            });
                            return;
                        }
                        const tabTitleId = TAB_STRATEGY_BASIC_GRAY, tabContentId = 'tabContentBasicGray';
                        $('#tabTitle').append('<li style="float:left;width:70px" id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span style="color: black"><img width="18" height="18" src="${ctx}/images/graph/service_yellow_64.png">&nbsp;灰度兜底</span></li>');
                        $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="basicGray layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#basicGrayTemplate').html().replaceAll('$_INDEX_$', ''));
                        element.render(TAB);
                        initRateGrid('gridBasicGrayRate', '', data);
                    }

                    function addTabGray(condition, rate) {
                        tabIndex++;
                        const tabTitleId = TAB_STRATEGY_GRAY + tabIndex, tabContentId = 'tabContent' + tabIndex;
                        $('#tabTitle').append('<li style="float:left;width:70px" id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span style="color: black"><img width="18" height="18" src="${ctx}/images/graph/service_black_64.png">&nbsp;灰度<b>' + tabIndex + '</b></span></li>');
                        $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="gray layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#grayTemplate').html().replaceAll('$_INDEX_$', tabIndex));
                        element.render(TAB);
                        initConditionGrid('gridGray', tabIndex, condition);
                        initRateGrid('gridGrayRate', tabIndex, rate);
                    }

                    function bindRouteSelect(id, data) {
                        let basicStrategyRouteId = '${entity.basicStrategyRouteId!''}';
                        if (data) basicStrategyRouteId = data;
                        const routes = admin.getRoutes(${strategyType});
                        const sel = $('#' + id);
                        sel.html('<option value="">请选择链路标识</option>');
                        $.each(routes, function (key, val) {
                            let option, v = val.routeId;
                            const k = val.routeId;
                            if (val.description != '') {
                                v = v + ' (' + val.description + ')';
                            }
                            if (basicStrategyRouteId == k) {
                                option = $("<option>").attr('selected', 'selected').val(k).text(v);
                            } else {
                                option = $("<option>").val(k).text(v);
                            }
                            sel.append(option);
                        });
                        layui.form.render('select');
                    }

                    function newConditionRow(data) {
                        const result = [];
                        if (data) {
                            for (let i = 0; i < data.length; i++) {
                                conditionCount++;
                                result.push({
                                    'index': conditionCount,
                                    'parameterName': data[i].parameterName,
                                    'operator': data[i].operator,
                                    'value': data[i].value,
                                    'logic': data[i].logic
                                });
                            }
                            if (result.length < 1) {
                                conditionCount++;
                                result.push({
                                    'index': conditionCount,
                                    'parameterName': '',
                                    'operator': '==',
                                    'value': '',
                                    'logic': 'and'
                                });
                            }
                        } else {
                            conditionCount++;
                            result.push({
                                'index': conditionCount,
                                'parameterName': '',
                                'operator': '==',
                                'value': '',
                                'logic': 'and'
                            });
                        }
                        return result;
                    }

                    function newHeaderRow(data) {
                        const result = [];
                        if (data) {
                            for (let i = 0; i < data.length; i++) {
                                headerCount++;
                                result.push({
                                    'index': headerCount,
                                    'headerName': data[i].headerName,
                                    'value': data[i].value
                                });
                            }
                            if (result.length < 1) {
                                headerCount++;
                                result.push({
                                    'index': headerCount,
                                    'headerName': '',
                                    'value': ''
                                });
                            }
                        } else {
                            headerCount++;
                            result.push({
                                'index': headerCount,
                                'headerName': '',
                                'value': ''
                            });
                        }
                        return result;
                    }

                    function newRateRow(data) {
                        const result = [];
                        const routeNames = admin.getRoutes(${strategyType});
                        if (data) {
                            for (let i = 0; i < data.length; i++) {
                                rateCount++;
                                result.push({
                                    'index': rateCount,
                                    'routeId': data[i].routeId,
                                    'rate': data[i].rate,
                                    'routeIdList': routeNames
                                });
                            }
                            if (result.length < 1) {
                                rateCount++;
                                result.push({
                                    'index': rateCount,
                                    'routeId': '',
                                    'rate': '',
                                    'routeIdList': routeNames
                                });
                            }
                            return result;
                        } else {
                            rateCount++;
                            result.push({
                                'index': rateCount,
                                'routeId': '',
                                'rate': '',
                                'routeIdList': routeNames
                            });
                        }
                        return result;
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
                                {field: 'parameterName', title: '参数名', unresize: true, edit: 'text', width: 267},
                                {title: '运算符', templet: '#tOperator' + tabIndex, unresize: true, width: 100},
                                {field: 'value', title: '值', edit: 'text', unresize: true, width: 267},
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

                    function initRateGrid(prefixGridId, tabIndex, defaultRate) {
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
                                {field: 'routeId', templet: '#templateRouteId' + tabIndex, unresize: true, title: '链路标识', width: 348},
                                {field: 'rate', title: '流量配比 [输入0 ~ 100的数字]', edit: 'text', unresize: true, width: 348},
                                {title: '操作', align: 'center', toolbar: '#grid-route-bar', unresize: true, width: 150}
                            ]],
                            data: newRateRow(defaultRate)
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

                    function isEmpty(val) {
                        return val == undefined || val == '' || val == '{}' || val == '[]' || val == '[{}]';
                    }

                    $('#callback').click(function () {
                        routeIds = [];
                        spels = new Set();
                        $('#error').val('');
                        if ($('#error').val() == '') {
                            collectBasicGlobalStrategy();
                        }
                        if ($('#error').val() == '') {
                            collectBasicBlueGreenStrategy();
                        }
                        if ($('#error').val() == '') {
                            collectBlueGreenStrategy();
                        }
                        if ($('#error').val() == '') {
                            collectBasicGrayStrategy();
                        }
                        if ($('#error').val() == '') {
                            collectGrayStrategy();
                        }
                        if ($('#error').val() == '') {
                            collectHeader();
                        }
                        $('#routeIds').val(JSON.stringify(admin.distinct(routeIds)));
                    });

                    function collectBasicGlobalStrategy() {
                        $('#basicGlobalStrategyRouteId').val('');
                        if (existBasicGlobal()) {
                            $('#basicGlobalStrategyRouteId').val($('#basicGlobalRouteId').val());
                            routeIds.push($('#basicGlobalStrategyRouteId').val());
                        }
                    }

                    function collectBasicBlueGreenStrategy() {
                        $('#basicBlueGreenStrategyRouteId').val('');
                        if (existBasicBlueGreen()) {
                            $('#basicBlueGreenStrategyRouteId').val($('#basicBlueGreenRouteId').val());
                            routeIds.push($('#basicBlueGreenStrategyRouteId').val());
                        }
                    }

                    function collectBlueGreenStrategy() {
                        $('#blueGreenStrategy').val('');
                        const all = {}, spelSet = new Set();
                        $('#tabContent').find('.blueGreen.layui-tab-item').each(function () {
                            const tabIndex = $(this).attr('tag');
                            const gridBlueGreen = 'gridBlueGreen' + tabIndex;
                            const spelCondition = $(this).find('#spelCondition' + tabIndex).val();

                            if (spels.has(spelCondition)) {
                                $('#error').val('条件设置已存在,请重新输入');
                                return false;
                            }
                            spels.add(spelCondition);

                            if (tabIndex && spelCondition != undefined) {
                                if (spelCondition == '') {
                                    $('#error').val('蓝绿' + tabIndex + '的条件设置不允许为空');
                                    return false;
                                } else if (spelSet.has(spelCondition)) {
                                    $('#error').val('蓝绿' + tabIndex + '的条件设置已存在,请重新输入');
                                    return false;
                                }
                                spelSet.add(spelCondition);

                                const _dataCondition = [], _setCondition = new Set(), spelSingleSet = new Set();
                                $.each(table.cache[gridBlueGreen], function (index, item) {
                                    if (item.parameterName != '' && item.value != '') {
                                        const k = item.parameterName + item.operator + item.value + item.logic;
                                        if (spelSingleSet.has(k)) {
                                            $('#error').val('蓝绿' + tabIndex + '的条件设置存在重复情况, 请检查后重新输入');
                                            return false;
                                        }
                                        spelSingleSet.add(k);

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
                                        $('#error').val('蓝绿' + tabIndex + '的参数名或值不允许为空');
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
                        if (existBasicGray()) {
                            const all = [], set = new Set(), routeIdSet = new Set();
                            let total = 0;
                            $.each(table.cache['gridBasicGrayRate'], function (index, item) {
                                if (item.routeId == '' || item.rate == '') {
                                    $('#error').val('灰度兜底的流量配比不允许为空');
                                    return false;
                                } else if (!admin.isInt(item.rate)) {
                                    $('#error').val('[' + item.rate + ']不是正整数, 请重新填写');
                                    return false;
                                }

                                if (routeIdSet.has(item.routeId)) {
                                    $('#error').val('灰度兜底的链路标识存在重复, 请检查后重新选择');
                                    return false;
                                }
                                routeIdSet.add(item.routeId);

                                total = total + parseInt(item.rate);
                                const json = {'routeId': item.routeId, 'rate': item.rate}, key = JSON.stringify(json);
                                if (!set.has(key)) {
                                    set.add(key);
                                    all.push(json);
                                }
                            });

                            if (total != 100) {
                                $('#error').val('灰度兜底的流量配比相加必须等于100, 请重新填写');
                                return false;
                            }

                            $('#basicGrayStrategy').val(JSON.stringify(all));
                        }
                    }

                    function collectGrayStrategy() {
                        $('#grayStrategy').val('');
                        const all = {}, spelSet = new Set();
                        $('#tabContent').find('.gray.layui-tab-item').each(function () {
                            const tabIndex = $(this).attr('tag');
                            const gridGray = 'gridGray' + tabIndex, gridGrayRate = 'gridGrayRate' + tabIndex;
                            const spelCondition = $(this).find('#spelCondition' + tabIndex).val();
                            if (spels.has(spelCondition)) {
                                $('#error').val('条件设置已存在,请重新输入');
                                return false;
                            }
                            spels.add(spelCondition);
                            if (tabIndex && spelCondition != undefined) {
                                if (spelCondition == '') {
                                    $('#error').val('灰度' + tabIndex + '的条件设置不允许为空');
                                    return false;
                                } else if (spelSet.has(spelCondition)) {
                                    $('#error').val('灰度' + tabIndex + '的条件设置已存在,请重新输入');
                                    return false;
                                }
                                spelSet.add(spelCondition);

                                const _dataCondition = [], _setCondition = new Set(), spelSingleSet = new Set();
                                $.each(table.cache[gridGray], function (index, item) {
                                    if (item.parameterName != '' && item.value != '') {
                                        const k = item.parameterName + item.operator + item.value + item.logic;
                                        if (spelSingleSet.has(k)) {
                                            $('#error').val('灰度' + tabIndex + '的条件设置存在重复情况, 请检查后重新输入');
                                            return false;
                                        }
                                        spelSingleSet.add(k);

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
                                        $('#error').val('灰度' + tabIndex + '的参数名或值不允许为空');
                                        return false;
                                    }
                                });
                                if ($('#error').val() !== '') {
                                    return false;
                                }

                                const _rates = [], _setRates = new Set(), routeIdSet = new Set();
                                let total = 0;
                                $.each(table.cache[gridGrayRate], function (index, item) {
                                    if (item.routeId == '' || item.rate == '') {
                                        $('#error').val('灰度' + tabIndex + '的流量配比不允许为空');
                                        return false;
                                    } else if (!admin.isInt(item.rate)) {
                                        $('#error').val('灰度' + tabIndex + '流量配比[' + item.rate + ']不是正整数, 请重新填写');
                                        return false;
                                    }
                                    if (routeIdSet.has(item.routeId)) {
                                        $('#error').val('灰度' + tabIndex + '的链路标识存在重复, 请检查后重新选择');
                                        return false;
                                    }
                                    routeIdSet.add(item.routeId);

                                    total = total + parseInt(item.rate);
                                    const json = {'routeId': item.routeId, 'rate': item.rate}, key = JSON.stringify(json);
                                    routeIds.push(item.routeId);
                                    if (!_setRates.has(key)) {
                                        _setRates.add(key);
                                        _rates.push(json);
                                    }
                                });

                                if ($('#error').val() == '') {
                                    if (total != 100) {
                                        $('#error').val('灰度' + tabIndex + '的流量配比相加必须等于100, 请重新填写');
                                        return false;
                                    }
                                }
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
                            } else {
                                $('#error').val('');
                            }
                        });
                        $('#header').val(JSON.stringify(dataHeader));
                    }
                }
            )
        </script>
    </div>
</#macro>