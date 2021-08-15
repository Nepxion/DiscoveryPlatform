<@compress single_line=false>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">

        <style>
            /*.layui-form-pane .layui-form-label {
                float: left;
                display: block;
                padding: 9px 15px;
                width: 60px;
                font-weight: 400;
                line-height: 20px;
                text-align: right;
            }*/

            .layui-form-pane .layui-form-label {
                width: 85px;
            }

            .layui-form-item{
                display: flex;
            }

            .layui-table-col-special .layui-table-cell {
                padding: 0 0px;
            }
            .layui-form-pane .layui-input-inline {
                flex: 1;
            }
            .layui-form-item .layui-input-inline {
                margin-right: 0;
            }

            td .layui-form-select {
                margin-top: -10px;
                margin-left: -15px;
                margin-right: -15px;
            }

            .layui-form-select dl{
                z-index: 9999;
            }

            .layui-elem-field legend{
                margin-left: 5px;
            }
        </style>
    </head>
    <body>

    <div class="layui-fluid layui-form">
        <div class="layui-card">
            <div class="layui-card-body layui-col-md9">
                <div id="topology"></div>
            </div>

            <div class="layui-card-body layui-col-md3">
                <div class="layui-form layui-form-pane" lay-filter="settings">
                    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                        <legend>侦测入口</legend>
                    </fieldset>
                    <div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">类型</label>
                            <div class="layui-input-inline">
                                <select id="portalType" name="portalType" lay-filter="portalType">
                                    <option value="GATEWAY" selected="">网关</option>
                                    <option value="SERVICE">服务</option>
                                </select>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">协议</label>
                            <div class="layui-input-inline">
                                <select name="portalProtocol">
                                    <option value="http://" selected="">HTTP</option>
                                    <option value="https://">HTTPS</option>
                                </select>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">服务</label>
                            <div class="layui-input-inline">
                                <select id='portalService' name="portalService" lay-filter="portalService">
                                </select>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">实例</label>
                            <div class="layui-input-inline">
                                <select id='portalInstance' name="portalInstance" lay-filter="portalInstance">
                                </select>
                            </div>
                        </div>
                    </div>

                    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                        <legend>侦测参数</legend>
                    </fieldset>
                    <div class="layui-form-item">
                        <table class="layui-hide" id="gridInspectParam" lay-filter="gridInspectParam"></table>

                        <script type="text/html" id="paramTypeTpl">
                            <select lay-filter='paramType' lay-verify='required' lay-search>
                                {{# layui.each(d.paramTypeList, function(index, item){ }}
                                    <option value="{{ item }}" {{ d.paramType==item ? 'selected="selected"' : '' }}>
                                        {{ item }}
                                    </option>
                                {{# }); }}
                            </select>
                        </script>

                        <script type="text/html" id="grid-param-bar">
                            <div class="layui-btn-group">
                                <a class="layui-btn layui-btn-xs" lay-event="addParam">
                                    <i class="layui-icon">&#xe654;</i>
                                </a>
                                <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="removeParam">
                                    <i class="layui-icon">&#xe67e;</i>
                                </a>
                            </div>
                        </script>
                    </div>

                    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                        <legend>侦测链路</legend>
                    </fieldset>
                    <div class="layui-form-item">
                        <table class="layui-hide" id="gridService" lay-filter="gridService"></table>

                        <script type="text/html" id="serviceTpl">
                            <select lay-filter='service' lay-verify='required' lay-search>
                                <option value="">请选择链路服务</option>
                                    {{# layui.each(d.serviceList, function(index, item){ }}
                                        <option value="{{ item }}" {{ d.service==item ? 'selected="selected"' : '' }}>
                                            {{ item }}
                                        </option>
                                {{# }); }}
                            </select>
                        </script>

                        <script type="text/html" id="grid-service-bar">
                            <div class="layui-btn-group">
                                <a class="layui-btn layui-btn-xs" lay-event="addService">
                                    <i class="layui-icon">&#xe654;</i>
                                </a>
                                <a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="removeService">
                                    <i class="layui-icon">&#xe67e;</i>
                                </a>
                            </div>
                        </script>
                    </div>

                    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
                        <legend>侦测执行</legend>
                    </fieldset>
                    <div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">维度</label>
                            <div class="layui-input-inline">
                                <select name="dimension">
                                    <option value="version" selected="">版本</option>
                                    <option value="region">区域</option>
                                    <option value="group">分组</option>
                                    <option value="env">环境</option>
                                    <option value="zone">可用区</option>
                                </select>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <label class="layui-form-label">次数</label>
                            <div class="layui-input-inline">
                                <select name="requestTimes">
                                    <option value="200">200</option>
                                    <option value="500" selected="">500</option>
                                    <option value="1000">1000</option>
                                    <option value="2000">2000</option>
                                    <option value="5000">5000</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <button type="button" class="layui-btn layui-btn-normal" id="execute">执行</button>
                    <button type="button" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </div>
    </div>

    <script>/*Fixing iframe window.innerHeight 0 issue in Safari*/document.body.clientHeight;</script>
    <script src="${ctx}/js/g6/g6.min.js"></script>
    <script src="${ctx}/js/g6/build.g6.js"></script>
    <script src="${ctx}/js/g6/dagre.min.js"></script>
    <script src="${ctx}/js/g6/build.plugins.js"></script>
    <!--拓扑图-->
    <script>
        /**
         * node 特殊属性
         */
        let nodeExtraAttrs = {
            begin: {
                fill: "#9FD4FB",
            },
            normal: {
                fill: "#9FFBE6",
            },
            end: {
                fill: "#C2E999"
            }
        };

        /**
         * 自定义节点
         */
        G6.registerNode("node", {
            drawShape: function drawShape(cfg, group) {
                if (cfg.type === 'begin') {
                    return group.addShape('image', {
                        attrs: {
                            x: -24,
                            y: 60,
                            width: 48,
                            height: 48,
                            img: "${ctx}/images/graph/gateway_black_64.png"
                        }
                    });
                }

                let rect = group.addShape('image', {
                    attrs: {
                        x: -16,
                        y: 0,
                        width: 36,
                        height: 36,
                        img: "${ctx}/images/graph/service_green_64.png"
                    }
                });

                group.addShape('text', {
                    attrs: {
                        x: -60,
                        y: 75,
                        text: cfg.id ? cfg.id : '',
                        fill: cfg.textColor ? cfg.textColor : '#666666',
                        autoRotate: true,
                    }
                });

                group.addShape('text', {
                    attrs: {
                        x: -60,
                        y: 95,
                        text: cfg.dimensionValue ? cfg.dimension + ' = ' + cfg.dimensionValue : '',
                        fill: cfg.textColor ? cfg.textColor : '#666666',
                        autoRotate: true,
                    }
                });

                return rect;
            },
            setState: function setState(name, value, item) {
                let group = item.getContainer();
                let shape = group.get("children")[0];

                if (name === "selected") {
                    if (value) {
                        shape.attr("fill", "#F6C277");
                    } else {
                        shape.attr("fill", "#FFD591");
                    }
                }
            },

            getAnchorPoints: function getAnchorPoints() {
                return [[0.5, 0], [0.5, 1]];
            }
        }, "single-shape");

        /**
         * 自定义 edge 中心关系节点
         */
        G6.registerNode("statusNode", {
            drawShape: function drawShape(cfg, group) {
                let circle = group.addShape("circle", {
                    attrs: {
                        x: 0,
                        y: 0,
                        r: 6,
                        fill: cfg.active ? "#AB83E4" : "#ccc"
                    }
                });
                return circle;
            }
        }, "single-shape");

        /**
         * 自定义带箭头的贝塞尔曲线 edge
         */
        G6.registerEdge("line-with-arrow", {
            itemType: "edge",
            draw: function draw(cfg, group) {
                let startPoint = cfg.startPoint;
                let endPoint = cfg.endPoint;
                let centerPoint = {
                    x: (startPoint.x + endPoint.x) / 2,
                    y: (startPoint.y + endPoint.y) / 2
                };
                let controlPoint = {
                    x: (startPoint.x),
                    y: (startPoint.y + 20)
                };

                let color = "#00A3AF";
                let path = group.addShape("path", {
                    attrs: {
                        path: [
                            ["M", startPoint.x, startPoint.y],
                            ["Q", controlPoint.x, controlPoint.y + 8, centerPoint.x, centerPoint.y],
                            ["T", endPoint.x, endPoint.y - 8],
                            ["L", endPoint.x, endPoint.y]],
                        stroke: color,
                        lineWidth: 1.6,
                        endArrow: {
                            path: "M -3,0 L 3,3 L 3,-3 Z",
                            d: -4,
                            lineWidth: 3,
                        }
                    }
                });

                let source = cfg.source,
                    target = cfg.target;

                group.addShape('text', {
                    attrs: {
                        id: "statusNode" + source + "-" + target,
                        r: 6,
                        x: centerPoint.x,
                        y: centerPoint.y + (cfg.source === graph.beginId ? 55 : 35),
                        text: cfg.count ? 'count = ' + cfg.count : '',
                        fill: cfg.textColor ? cfg.textColor : '#AB83E4',
                    }
                });

                return path;
            }
        });

        let graph = new G6.Graph({
            container: "topology",
            width: 900,
            height: 800,
            layout: {
                type: "dagre",
                nodesep: 100,
                ranksepFunc : (d) => {
                    if (d.type === "begin") {
                        return 150;
                    }
                    return 20;
                },
                controlPoints: true
            },
            modes: {
                default: ['drag-canvas', 'zoom-canvas', 'drag-node']
            },
            defaultNode: {
                shape: "node",
                labelCfg: {
                    style: {
                        fill: "#666666",
                        fontSize: 14
                    },
                    offset: 30,
                    position: 'bottom'
                }
            },
            defaultEdge: {
                shape: "line-with-arrow",
                style: {
                    endArrow: true,
                    lineWidth: 2,
                    stroke: "#ccc"
                }
            }
        });
    </script>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
            const admin = layui.admin, $ = layui.$, form = layui.form, table = layui.table, layer = layui.layer;
            let thatAdmin = null;
            let portalType = 'GATEWAY', portalServiceList = [], portalInstanceList = [], serviceList = [];

            setTimeout(function () {
                thatAdmin = layui.admin;
                refreshPortalService();

                addService();
                form.render('select');
            }, 100);

            // =================== 侦测入口 =============================
            form.on('select(portalType)', function (opt) {
                portalType = opt.value;
                refreshPortalService();
            });

            form.on('select(portalService)', function (opt) {
                let portalService = opt.value;
                refreshPortalInstance(portalService);
            });

            function refreshPortalService() {
                portalServiceList = [];
                const portalTypeInt = portalType === 'GATEWAY' ? 1 : 2;
                admin.post('/blue-green/do-list-portal-names', {'portalTypeInt': portalTypeInt}, function (result) {
                    const portalInstance = $("select[name=portalInstance]");
                    portalInstance.html('<option value="">请选择实例</option>');
                    const portalService = $("select[name=portalService]");
                    let portalTypeName = '';
                    if (portalTypeInt === 1) {
                        portalTypeName = '网关';
                    } else if (portalTypeInt === 2) {
                        portalTypeName = '服务';
                    }
                    portalService.html('<option value="">请选择' + portalTypeName + '名称</option>');
                    $.each(result.data, function (index, item) {
                        let option = $("<option>").val(item).text(item);
                        portalService.append(option);
                        portalServiceList.push(item)
                    });
                    layui.form.render('select');
                });
            }

            function refreshPortalInstance(serviceName) {
                portalInstanceList = [];
                admin.post('/blue-green/do-list-service-metadata', {'serviceName': serviceName}, function (response) {
                    const portalInstance = $("select[name=portalInstance]");
                    portalInstance.html('<option value="">请选择实例</option>');
                    $.each(response.data, function (index, item) {
                        const instance = item.host + ':' + item.port;
                        let option = $("<option>").val(instance).text(instance);
                        portalInstance.append(option);
                        portalInstanceList.push(item);
                    });
                    layui.form.render('select');
                });
            }

            // =================== 侦测参数 =============================
            table.render({
                elem: '#gridInspectParam'
                , cellMinWidth: 60
                , page: false
                , limit: 99999999
                , limits: [99999999]
                , even: false
                , loading: false
                , cols: [[
                    {title: '类型', field: 'paramType', unresize: true, templet: '#paramTypeTpl', width: '30%'},
                    {title: '键', field: 'key', unresize: true, edit: 'text', width: '20%'},
                    {title: '值', field: 'value', unresize: true, edit: 'text', width: '20%'},
                    {title: '操作', align: 'center', unresize: true, toolbar: '#grid-param-bar', width: '30%'}
                ]]
                , done: function (res, curr, count) {
                    $(".layui-table-body, .layui-table-box, .layui-table-cell").css('overflow', 'visible');
                }
                , data: [newInspectParamRow()]
            });

            table.on('tool(gridInspectParam)', function (obj) {
                const gd = table.cache['gridInspectParam'];
                if (obj.event === 'addParam') {
                    gd.push(newInspectParamRow());
                    reload('gridInspectParam', gd);
                    $('div[class="layui-table-mend"]').remove();
                } else if (obj.event === 'removeParam') {
                    if (gd.length > 1) {
                        $.each(gd, function (i, item) {
                            if (item && item.index == obj.data.index) {
                                gd.remove(item);
                            }
                        });
                        reload('gridInspectParam', gd);
                        $('div[class="layui-table-mend"]').remove();
                    }
                }
            });

            form.on('select(paramType)', function (obj) {
                const gridId = 'gridInspectParam';
                const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                const gd = table.cache[gridId];
                gd[dataIndex]['paramType'] = obj.value;
                reload(gridId, gd);
            });

            function collectParams(data) {
                data = data == null ? {} : data;
                const headerParam = {}, paramParam = {}, cookieParam = {};
                const headerSet = new Set(), paramSet = new Set(), cookieSet = new Set();
                $.each(table.cache['gridInspectParam'], function (index, item) {
                    const paramType = item.paramType;
                    const key = item.key;
                    const value = item.value;
                    if (key && value) {
                        if(paramType === 'Header') {
                            if (!headerSet.has(key)) {
                                headerSet.add(key);
                                headerParam[key] = value;
                            }
                        } else if(paramType === 'Parameter') {
                            if (!paramSet.has(key)) {
                                paramSet.add(key);
                                paramParam[key] = value;
                            }
                        } else if(paramType === 'Cookie') {
                            if (!cookieSet.has(key)) {
                                cookieSet.add(key);
                                cookieParam[key] = value;
                            }
                        }
                    }
                });
                data.headers = headerParam;
                data.parameters = paramParam;
                data.cookies = cookieParam;
            }

            function newInspectParamRow() {
                const paramTypeList = ['Header', 'Parameter', 'Cookie'];
                return {
                    paramType: paramTypeList[0],
                    paramTypeList: paramTypeList,
                    key: '',
                    value: ''
                };
            }

            // =================== 侦测链路 =============================
            function addService () {
                table.render({
                    elem: '#gridService'
                    , cellMinWidth: 60
                    , page: false
                    , limit: 99999999
                    , limits: [99999999]
                    , even: false
                    , loading: false
                    , cols: [[
                        {title: '服务', field: 'service', unresize: true, templet: '#serviceTpl', width: '70%'},
                        {title: '操作', align: 'center', unresize: true, toolbar: '#grid-service-bar', width: '30%'}
                    ]]
                    , done: function (res) {
                        $(".layui-table-body, .layui-table-box, .layui-table-cell").css('overflow', 'visible');
                    }
                    , data: [newServiceRow()]
                });

                table.on('tool(gridService)', function (obj) {
                    const gd = table.cache['gridService'];
                    if (obj.event === 'addService') {
                        gd.push(newServiceRow());
                        reload('gridService', gd);
                        $('div[class="layui-table-mend"]').remove();
                    } else if (obj.event === 'removeService') {
                        if (gd.length > 1) {
                            $.each(gd, function (i, item) {
                                if (item && item.index == obj.data.index) {
                                    gd.remove(item);
                                }
                            });
                            reload('gridService', gd);
                            $('div[class="layui-table-mend"]').remove();
                        }
                    }
                });

                form.on('select(service)', function (obj) {
                    const gridId = 'gridService';
                    const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                    const gd = table.cache[gridId];
                    gd[dataIndex]['service'] = obj.value;
                    reload(gridId, gd);
                });
            }

            function collectServiceChain(data) {
                data = data == null ? {} : data;
                const serviceChain = [];
                const serviceChainSet = new Set();
                $.each(table.cache['gridService'], function (index, item) {
                    const service = item.service;
                    if (service) {
                        if (!serviceChainSet.has(service)) {
                            serviceChainSet.add(service);
                            serviceChain.push(service);
                        }
                    }
                });
                data.serviceChain = serviceChain;
            }

            function newServiceRow() {
                refreshServiceOptions();
                return {
                    service: serviceList[0] ? serviceList[0] : null,
                    serviceList: serviceList,
                };
            }

            function refreshServiceOptions() {
                const set = new Set();
                serviceList = [];
                admin.post('/blue-green/do-list-service-names', {}, function (response) {
                    $.each(response.data, function (index, item) {
                        if (!set.has(item)) {
                            set.add(item);
                            serviceList.push(item);
                        }
                    });
                }, null, false);
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

            // =================== 表单提交 =============================
            $('#execute').on('click', function() {
                const data = form.val('settings');
                collectParams(data);
                collectServiceChain(data);

                $.ajax({
                    url: '/inspector/simulation',
                    type: 'POST',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    cache: false,
                    beforeSend: function (xhr, settings) {
                        admin.beforeRequest(xhr, settings);
                    },
                    complete: function (xhr) {
                        admin.afterResponse(null, null, xhr);
                        const result = xhr.responseJSON;
                        if (result && result.ok) {
                            console.log("success", result);
                            let data = result.data || {nodes: [{id: "noConfig", label: "未配置"}]};

                            renderGraph(data)
                        } else {
                            console.log("error", result);
                        }
                    }
                });
            });

            var beginId;
            function renderGraph(data) {
                for (var i = 0; i < data.nodes.length; i ++) {
                    if (data.nodes[i].type === 'begin') graph.beginId = data.nodes[i].id;
                }

                graph.data(data);
                graph.render();

                setTimeout(function() {
                    graph.fitView();
                }, 100);
            }
        });
    </script>
    </body>
    </html>
</@compress>