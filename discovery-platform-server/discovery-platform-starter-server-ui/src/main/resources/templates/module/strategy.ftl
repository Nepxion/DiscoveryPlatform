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
                <label class="layui-form-label">条件策略</label>
                <div class="layui-input-block">
                    <a id="btnAddBasicBlueGreen" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加兜底蓝绿</a>
                    <a id="btnAddBlueGreen" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加条件蓝绿</a>
                    <a id="btnRemoveStrategy" class="layui-btn layui-btn-sm layui-btn-danger"><i class="layui-icon">&#xe640;</i>删除策略</a>
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
                <span class="layui-badge layui-bg-blue">选择蓝绿兜底链路</span>

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
                <span class="layui-badge layui-bg-blue">条件设置</span>
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

                <span class="layui-badge layui-bg-blue" style="margin-top:15px;">选择链路</span>

                <div class="layui-row" style="margin-top: 10px;">
                    <div class="layui-col-md11">
                        <select id="routeId$_INDEX_$" autocomplete="off" class="layui-select" lay-search>
                        </select>
                    </div>
                    <div class="layui-col-md1">
                        <a id="btnReloadRoute$_INDEX_$" tag="routeId$_INDEX_$" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                            <i class="layui-icon">&#xe669;</i>
                        </a>
                    </div>
                </div>
            </div>

            <input type="hidden" id="id" name="id" value="${entity.id!''}"/>
            <input type="hidden" id="error" name="error" value=""/>
            <input type="hidden" id="routeIds" name="routeIds" value=""/>
            <input type="hidden" id="basicBlueGreenStrategyRouteId" name="basicBlueGreenStrategyRouteId" value=""/>
            <input type="hidden" id="blueGreenStrategy" name="blueGreenStrategy" value=""/>
            <input type="hidden" id="header" name="header" value=""/>
        </div>
        <script>
            layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
                    const form = layui.form, admin = layui.admin, $ = layui.$, element = layui.element, table = layui.table;
                    const TAB = 'tab', TAB_STRATEGY = 'tabStrategy', TAB_STRATEGY_BASIC_BLUE_GREEN = TAB_STRATEGY + 'BasicBlueGreen';
                    let portalType = 1, tabIndex = 0, tabSelectTitle = '', tabSelect, headerCount = 0, blueGreenCount = 0;
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
                        for (const k in blueGreenStrategy) {
                            const condition = blueGreenStrategy[k].condition;
                            const routeId = blueGreenStrategy[k].routeId;
                            addTabBlueGreen(condition, routeId);
                        }
                        if (basicBlueGreenStrategyRouteId != '') {
                            addTabBasicBlueGreen(basicBlueGreenStrategyRouteId);
                            element.tabChange(TAB, TAB_STRATEGY_BASIC_BLUE_GREEN);
                        } else {
                            element.tabChange(TAB, TAB_STRATEGY + 1);
                        }
                        </#if>

                    }, 50);

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
                        element.tabChange(TAB, TAB_STRATEGY + tabIndex);
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

                    function addTabBasicBlueGreen(data) {
                        if (existBasicBlueGreen()) {
                            element.tabChange(TAB, TAB_STRATEGY_BASIC_BLUE_GREEN);
                            admin.success('系统操作', '已存在兜底策略');
                            return;
                        }
                        const tabTitleId = TAB_STRATEGY_BASIC_BLUE_GREEN, tabContentId = 'tabContentBasicBlueGreen';
                        $('#tabTitle').prepend('<li id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span style="color: blue">蓝绿兜底</span></li>');
                        $('#tabContent').prepend('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#basicBlueGreenTemplate').html());
                        element.render(TAB);
                        $('#btnRefreshBlueGreenRouteId').click(function () {
                            bindRouteSelect($(this).attr('tag'), data);
                        });
                        $('#btnRefreshBlueGreenRouteId').click();
                    }

                    function addTabBlueGreen(condition, routeId) {
                        tabIndex++;
                        const tabTitleId = TAB_STRATEGY + tabIndex, tabContentId = 'tabContent' + tabIndex, gridCondition = 'gridCondition' + tabIndex, btnReloadRoute = 'btnReloadRoute' + tabIndex;
                        $('#tabTitle').append('<li id="' + tabTitleId + '" lay-id="' + tabTitleId + '"><span style="color: blue">蓝绿策略<b>' + tabIndex + '</b></span></li>');
                        $('#tabContent').append('<div id="' + tabContentId + '" tag="' + tabIndex + '" class="layui-tab-item"></div>');
                        $('#' + tabContentId).append($('#blueGreenTemplate').html().replaceAll('$_INDEX_$', tabIndex));
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
                            data: newBlueGreenRow(condition)
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
                        $('#' + btnReloadRoute).click(function () {
                            bindRouteSelect($(this).attr('tag'), routeId);
                        });
                        $('#' + btnReloadRoute).click();
                        $('#btnAssemble' + tabIndex).click();
                    }

                    function bindRouteSelect(id, data) {
                        admin.loading(function () {
                            let basicStrategyRouteId = '${entity.basicStrategyRouteId!'null'}';
                            if (data) basicStrategyRouteId = data;
                            const routeNames = admin.getRoutes();
                            const sel = $('#' + id);
                            sel.html('<option value="">请选择链路编排标识</option>');
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

                    function newBlueGreenRow(data) {
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
                        collectBasicBlueGreenStrategy();
                        collectBlueGreenStrategy();
                        collectHeader();
                    });

                    function collectBasicBlueGreenStrategy() {
                        $('#basicBlueGreenStrategyRouteId').val('');
                        if (existBasicBlueGreen()) {
                            $('#basicBlueGreenStrategyRouteId').val($('#basicBlueGreenRouteId').val());
                        }
                    }

                    function collectBlueGreenStrategy() {
                        $('#blueGreenStrategy').val('');
                        $('#routeIds').val('');
                        const all = {};
                        const routeIds = [], routeIdSet = new Set();
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
                                        $('#error').val('蓝绿策略' + tabIndex + '的条件策略的参数名或值不允许为空');
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
                                }

                                if (routeId && !routeIdSet.has(routeId)) {
                                    routeIdSet.add(routeId);
                                    routeIds.push(routeId);
                                }
                            }
                        });
                        $('#blueGreenStrategy').val(JSON.stringify(all));
                        routeIds.push($('#basicBlueGreenStrategyRouteId').val());
                        $('#routeIds').val(JSON.stringify(routeIds));
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