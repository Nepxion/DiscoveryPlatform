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
                    <input type="text" name="description" class="layui-input" placeholder="请输入该${((type!'')=='VERSION')?string('版本','区域')}蓝绿的描述信息" autocomplete="off">
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">兜底策略</label>
                <div class="layui-input-inline" style="width: 850px">
                    <div class="layui-row">
                        <div class="layui-col-md11">
                            <select id="basicRouteId" name="basicRouteId" autocomplete="off" class="layui-select" lay-search>
                            </select>
                        </div>
                        <div class="layui-col-md1">
                            <a id="btnRefreshRouteId" tag="basicRouteId" class="layui-btn layui-btn-sm" style="margin-left: 10px;width:60px;margin-top: 4px">
                                <i class="layui-icon">&#xe669;</i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">条件策略</label>
                <div class="layui-input-block">
                    <a id="btnAddBlueGreen" class="layui-btn layui-btn-sm"><i class="layui-icon">&#xe654;</i>添加蓝绿</a>
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
            <input type="hidden" id="blueGreenStrategy" name="blueGreenStrategy" value=""/>
            <input type="hidden" id="header" name="header" value=""/>
        </div>
        <script>
            layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
                    const form = layui.form, admin = layui.admin, $ = layui.$, element = layui.element, table = layui.table;
                    const TAB = 'tab', TAB_CONDITION = 'tabCondition';
                    let portalType = 1, tabIndex = 0, tabSelectTitle = '', headerCount = 0, blueGreenCount = 0;

                    setTimeout(function () {
                        $('#btnRefreshPortal').click();
                        $('#btnRefreshRouteId').click();
                        addTabCondition();
                        addTabCondition();
                        element.tabChange(TAB, TAB_CONDITION + 1);
                    }, 50);

                    form.on('radio(portalType)', function (opt) {
                        portalType = opt.value;
                        $('#btnRefreshPortal').click();
                    });

                    element.on('tab(tab)', function () {
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
                        data: newHeaderRow()
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

                    $('#btnRefreshRouteId').click(function () {
                        bindRouteSelect($(this).attr('tag'));
                    });

                    $('#btnAddBlueGreen').click(function () {
                        addTabCondition();
                    });

                    $('#btnRemoveStrategy').click(function () {
                        layer.confirm('确定要删除 [' + tabSelectTitle + '] 吗?', function (index) {
                            element.tabDelete(TAB, tabSelect);
                            layer.close(index);
                        });
                    });

                    function addTabCondition() {
                        tabIndex++;
                        const tabTitleId = TAB_CONDITION + tabIndex, tabContentId = 'tabContent' + tabIndex, gridCondition = 'gridCondition' + tabIndex, btnReloadRoute = 'btnReloadRoute' + tabIndex;
                        $('#tabTitle').append('<li id="' + tabTitleId + '" lay-id="' + tabTitleId + '">蓝绿策略' + tabIndex + '</li>');
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
                            data: newBlueGreenRow()
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
                            bindRouteSelect($(this).attr('tag'));
                        });
                        $('#' + btnReloadRoute).click();
                    }

                    function bindRouteSelect(id) {
                        admin.loading(function () {
                            const routeNames = admin.getRoutes();
                            const sel = $('#' + id);
                            sel.html('<option value="">请选择链路编排标识</option>');
                            $.each(routeNames, function (key, val) {
                                let option = $("<option>").val(val).text(val);
                                sel.append(option);
                            });
                            layui.form.render('select');
                        });
                    }

                    function newBlueGreenRow() {
                        blueGreenCount++;
                        return [{
                            'index': blueGreenCount,
                            'parameterName': '',
                            'operator': '==',
                            'value': '',
                            'logic': 'and'
                        }];
                    }

                    function newHeaderRow() {
                        headerCount++;
                        return [{
                            'index': headerCount,
                            'headerName': '',
                            'value': ''
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

                    $('#callback').click(function () {
                        collectBlueGreenStrategy();
                        collectHeader();
                    });

                    function collectBlueGreenStrategy() {
                        $('#blueGreenStrategy').val('');
                        $('#routeIds').val('');
                        const all = {};
                        const routeIds = [];
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

                                all['cr' + tabIndex] = {
                                    'condition': _dataCondition,
                                    'routeId': routeId
                                };
                                routeIds.push(routeId);
                            }
                        });
                        $('#blueGreenStrategy').val(JSON.stringify(all));
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
    </body>
    </html>
</@compress>