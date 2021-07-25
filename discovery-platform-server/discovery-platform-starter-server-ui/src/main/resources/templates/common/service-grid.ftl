<#macro serviceGrid id="grid" metadataType="version">

    <table id="${id}" lay-filter="${id}"></table>

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
                <a class="layui-btn layui-btn-sm" lay-event="refresh">
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

    <script type="text/javascript">
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
            const admin = layui.admin, $ = layui.$, form = layui.form, table = layui.table;
            let rowCount = 0;
            tableErrorHandler();

            setTimeout(function () {
                table.render({
                    elem: '#${id}',
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
                        {title: '${((metadataType!'')=='version')?string('版本号','区域')}', field: 'serviceValue', templet: '#templateServiceValue', unresize: true},
                        {title: '操作', align: 'center', toolbar: '#grid-route-bar', unresize: true, width: 160}
                    ]],
                    data: [newServiceRow()]
                });

                table.on('tool(${id})', function (obj) {
                    const gd = table.cache['${id}'];
                    if (obj.event === 'refresh') {
                        layer.load();
                        const serviceNameList = admin.refreshServiceName();
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
                        dataRow['valueList'] = admin.refreshServiceMetadata(serviceName, 'version');
                        reload('${id}', gd);
                        layer.closeAll('loading');
                    } else if (obj.event === 'add') {
                        gd.push(newServiceRow());
                        reload('${id}', gd);
                        $('div[class="layui-table-mend"]').remove();
                    } else if (obj.event === 'remove') {
                        if (gd.length > 1) {
                            $.each(gd, function (i, item) {
                                if (item && item.index == obj.data.index) {
                                    gd.remove(item);
                                }
                            });
                            reload('${id}', gd);
                            $('div[class="layui-table-mend"]').remove();
                        }
                    }
                });

                form.on('select(serviceName)', function (obj) {
                    const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                    const gd = table.cache['${id}'];
                    const serviceName = obj.value;
                    gd[dataIndex]['serviceName'] = serviceName;
                    gd[dataIndex]['serviceValueList'] = admin.refreshServiceMetadata(serviceName, 'version');
                    reload('${id}', gd);
                });

                form.on('select(serviceValue)', function (obj) {
                    const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                    const gd = table.cache['${id}'];
                    gd[dataIndex]['serviceValue'] = obj.value;
                    reload('${id}', gd);
                });
            }, 100);

            function newServiceRow() {
                rowCount++;
                return {
                    'index': rowCount,
                    'serviceName': '',
                    'serviceValue': '',
                    'serviceNameList': admin.refreshServiceName(),
                    'serviceValueList': []
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
        });
    </script>
</#macro>