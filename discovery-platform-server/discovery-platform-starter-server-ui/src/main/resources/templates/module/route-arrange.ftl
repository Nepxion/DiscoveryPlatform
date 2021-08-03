<#macro routeArrange>
    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin"
         style="padding: 20px 30px 0 0;">
        <div class="layui-form-item">
            <label class="layui-form-label">链路标识</label>
            <div class="layui-input-inline" style="width: 1000px">
                <input type="text" id="routeId" name="routeId" class="layui-input" readonly value="${entity.routeId!''}">
                <input type="hidden" id="id" name="id" value="${entity.id!''}"/>
                <input type="hidden" id="serviceArrange" name="serviceArrange"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">描述信息</label>
            <div class="layui-input-inline" style="width: 1000px">
                <input type="text" id="description" name="description" class="layui-input" placeholder="请输入该条链路的描述信息" autocomplete="off" value="${entity.description!''}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">策略类型</label>
            <div class="layui-input-block">
                <input type="radio" name="strategyType" lay-filter="strategyType" value="1" title="版本" ${((entity.strategyType!'1')==1) ? string('checked','')}>
                <input type="radio" name="strategyType" lay-filter="strategyType" value="2" title="区域" ${((entity.strategyType!'1')==2) ? string('checked','')}>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">链路列表</label>
            <div class="layui-input-inline" style="width: 1000px">
                <table id="grid" lay-filter="grid"></table>

                <script type="text/html" id="templateServiceName">
                    <select name='serviceName' lay-filter='serviceName' lay-search>
                        <option value="">请选择服务名称</option>
                        {{# layui.each(d.serviceNameList, function(index, item){ }}
                        <option value="{{ item }}" {{ d.serviceName==item ?
                        'selected="selected"' : '' }}>
                        {{ item }}
                        </option>
                        {{# }); }}
                    </select>
                </script>

                <script type="text/html" id="templateServiceValue">
                    <select name='serviceValue' lay-filter='serviceValue' lay-search>
                        <option value="">请选择${((metadataType!'')=='version')?string('版本号','区域')}</option>
                        {{# layui.each(d.serviceValueList, function(index, item){ }}
                        <option value="{{ item }}" {{ d.serviceValue==item ?
                        'selected="selected"' : '' }}>
                        {{ item }}
                        </option>
                        {{# }); }}
                    </select>
                </script>

                <script type="text/html" id="grid-route-bar">
                    <div class="layui-btn-group">
                        <@select>
                            <a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="refresh">
                                <i class="layui-icon">&#xe669;</i>
                            </a>
                        </@select>
                        <@update>
                            <a class="layui-btn layui-btn-sm" lay-event="add">
                                <i class="layui-icon">&#xe654;</i>
                            </a>
                        </@update>

                        <@delete>
                            <a class="layui-btn layui-btn-warm layui-btn-sm" lay-event="remove">
                                <i class="layui-icon">&#xe67e;</i>
                            </a>
                        </@delete>
                    </div>
                </script>
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <input id="callback" type="button" style="display: none"/>

    <script type="text/javascript">
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const admin = layui.admin, $ = layui.$, form = layui.form, table = layui.table;
            let rowCount = 0, metadataName = 'version', strategyType = null;
            tableErrorHandler();
            const serviceArrange = ${((entity.serviceArrange!'')?length>0)?string((entity.serviceArrange!''),'[]')};
            setTimeout(function () {
                metadataName = admin.getMetadataName(${entity.strategyType!'1'});
                form.on('radio(strategyType)', function (data) {
                    let needUpdate = false;
                    strategyType = data.value;
                    if (data.value == 1 && metadataName != 'version') {
                        metadataName = 'version';
                        needUpdate = true;
                    } else if (data.value == 2 && metadataName != 'region') {
                        metadataName = 'region';
                        needUpdate = true;
                    }

                    if (needUpdate) {
                        admin.loading(function () {
                            const gd = table.cache['grid'];
                            $.each(gd, function (index, item) {
                                item['serviceValue'] = '';
                                item['serviceValueList'] = admin.getServiceMetadata(item['serviceName'], metadataName);
                            });
                            reload('grid', gd);
                        });
                    }
                });

                table.render({
                    elem: '#grid',
                    cellMinWidth: 80,
                    page: false,
                    limit: 99999999,
                    limits: [99999999],
                    even: true,
                    text: {
                        none: '暂无相关数据'
                    },
                    cols: [[
                        {type: 'numbers', title: '序号', unresize: true, width: 50},
                        {title: '服务名', field: 'serviceName', templet: '#templateServiceName', unresize: true},
                        {title: '版本', field: 'serviceValue', templet: '#templateServiceValue', unresize: true},
                        {title: '操作', align: 'center', toolbar: '#grid-route-bar', unresize: true, width: 160}
                    ]],
                    data: newServiceRow(serviceArrange),
                    done: function () {
                        if (strategyType == null) {
                            strategyType = ${entity.strategyType!'1'};
                        }
                        toggleStrategyType(strategyType);
                    }
                });

                table.on('tool(grid)', function (obj) {
                    const gd = table.cache['grid'];
                    if (obj.event === 'refresh') {
                        admin.loading(function () {
                            const serviceNameList = admin.getServiceName();
                            let serviceName = '';
                            let dataRow = null;
                            $.each(gd, function (index, item) {
                                if (item.index == obj.data.index) {
                                    dataRow = item;
                                    item['serviceNameList'] = serviceNameList;
                                    serviceName = item['serviceName'];
                                    return;
                                }
                            });
                            dataRow['serviceValueList'] = admin.getServiceMetadata(serviceName, metadataName);
                            reload('grid', gd);
                        });
                    } else if (obj.event === 'add') {
                        gd.push(newServiceRow()[0]);
                        reload('grid', gd);
                        $('div[class="layui-table-mend"]').remove();
                    } else if (obj.event === 'remove') {
                        if (gd.length > 1) {
                            $.each(gd, function (i, item) {
                                if (item && item.index == obj.data.index) {
                                    gd.remove(item);
                                }
                            });
                            reload('grid', gd);
                            $('div[class="layui-table-mend"]').remove();
                        }
                    }
                });

                form.on('select(serviceName)', function (obj) {
                    const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                    const gd = table.cache['grid'];
                    const serviceName = obj.value;
                    gd[dataIndex]['serviceName'] = serviceName;
                    gd[dataIndex]['serviceValueList'] = admin.getServiceMetadata(serviceName, metadataName);
                    reload('grid', gd);
                });

                form.on('select(serviceValue)', function (obj) {
                    const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                    const gd = table.cache['grid'];
                    gd[dataIndex]['serviceValue'] = obj.value;
                    reload('grid', gd);
                });
            }, 100);

            function newServiceRow(data) {
                const serviceNames = admin.getServiceName();
                if (data) {
                    const result = [];
                    for (let i = 0; i < data.length; i++) {
                        const serviceName = Object.keys(data[i])[0];
                        const serviceValue = data[i][serviceName];
                        rowCount++;
                        result.push({
                            'index': rowCount,
                            'serviceName': serviceName,
                            'serviceValue': serviceValue,
                            'serviceNameList': serviceNames,
                            'serviceValueList': admin.getServiceMetadata(serviceName, metadataName)
                        });
                    }
                    if (result.length < 1) {
                        rowCount++;
                        result.push({
                            'index': rowCount,
                            'serviceName': '',
                            'serviceValue': '',
                            'serviceNameList': serviceNames,
                            'serviceValueList': []
                        });
                    }
                    return result;
                } else {
                    rowCount++;
                    return [{
                        'index': rowCount,
                        'serviceName': '',
                        'serviceValue': '',
                        'serviceNameList': serviceNames,
                        'serviceValueList': []
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

            function toggleStrategyType(strategyType) {
                strategyType = parseInt(strategyType);
                if (strategyType == 1) {
                    $('th[data-field=serviceValue] > div.layui-table-cell > span').html('版本');
                } else if (strategyType == 2) {
                    $('th[data-field=serviceValue] > div.layui-table-cell > span').html('区域');
                } else {
                    $('th[data-field=serviceValue] > div.layui-table-cell > span').html('实例');
                }
            }

            $('#callback').click(function () {
                $('#serviceArrange').val('');

                const serviceArrange = [], set = new Set();
                $.each(table.cache['grid'], function (index, item) {
                    if (item['serviceName'] != '' && item['serviceValue'] != '') {
                        const key = item['serviceName'] + item['serviceValue'];
                        if (!set.has(key)) {
                            set.add(key);
                            const json = {};
                            json[item['serviceName']] = item['serviceValue'];
                            serviceArrange.push(json);
                        }
                    }
                });
                if (serviceArrange.length > 0) {
                    $('#serviceArrange').val(JSON.stringify(serviceArrange));
                }
            });
        });
    </script>
</#macro>