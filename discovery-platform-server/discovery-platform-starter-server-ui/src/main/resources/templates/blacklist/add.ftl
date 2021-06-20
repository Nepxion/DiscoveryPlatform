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
            <label class="layui-form-label">入口类型</label>
            <div class="layui-input-block">
                <input type="radio" lay-filter="portalType" name="portalType" value="1" title="网关类型" checked>
                <input type="radio" lay-filter="portalType" name="portalType" value="2" title="服务类型">
                <input type="radio" lay-filter="portalType" name="portalType" value="3" title="组类型">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">入口名称</label>
            <div class="layui-input-inline" style="width: 100px">
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
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">描述信息</label>
            <div class="layui-input-inline" style="width: 1000px">
                <input type="text" id="description" name="description" class="layui-input" placeholder="请输入该条黑名单的描述信息" autocomplete="off">
                <input type="hidden" id="serviceBlacklist" name="serviceBlacklist"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">黑名单类型</label>
            <div class="layui-input-block">
                <input type="radio" name="serviceBlacklistType" value="1" title="UUID黑名单" checked>
                <input type="radio" name="serviceBlacklistType" value="2" title="IP地址和端口黑名单">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label"">黑名单列表</label>
            <div class="layui-input-inline" style="width: 1000px">
                <table class="layui-hide" id="grid" lay-filter="grid"></table>

                <script type="text/html" id="tServiceName">
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

                <script type="text/html" id="tContent">
                    <select name="content" lay-filter='content' lay-search>
                        <option value="">请选择服务实例</option>
                        {{# layui.each(d.contents, function(index, item){ }}
                        <option uuid="{{ item.uuid }}" address="{{ item.host }}:{{item.port}}" value="{{ item.uuid }}" {{ d.uuid==item.uuid ?
                        'selected="selected"' : '' }}>{{ item.uuid}} &nbsp; | &nbsp; {{ item.host }}:{{item.port}}</option>
                        {{# }); }}
                    </select>
                </script>

                <script type="text/html" id="grid-bar">
                    <@update>
                        <div class="layui-btn-group">
                            <a class="layui-btn layui-btn-sm" lay-event="refresh">
                                <i class="layui-icon">&#xe669;</i>
                            </a>
                            <a class="layui-btn layui-btn-sm" lay-event="add">
                                <i class="layui-icon">&#xe654;</i>
                            </a>
                            <a class="layui-btn layui-btn-warm layui-btn-sm" lay-event="remove">
                                <i class="layui-icon">&#xe67e;</i>
                            </a>
                        </div>
                    </@update>
                </script>
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>

    <input id="callback" type="button" style="display: none"/>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const admin = layui.admin, form = layui.form, $ = layui.$, table = layui.table;
            let serviceNameList = [], portalType = 1, count = 0;

            setTimeout(function () {
                reloadPortalName(1);
                table.render({
                    elem: '#grid',
                    cellMinWidth: 80,
                    page: false,
                    limit: 99999999,
                    limits: [99999999],
                    even: false,
                    loading: false,
                    cols: [[
                        {type: 'numbers', title: '序号', width: 50},
                        {field: 'serviceName', title: '服务名称', unresize: true, templet: '#tServiceName'},
                        {field: 'uuid', title: '服务实例 [UUID&nbsp;|&nbsp;IP地址:端口]', unresize: true, templet: '#tContent', width: 440},
                        {title: '操作', align: 'center', toolbar: '#grid-bar', width: 150}
                    ]],
                    data: [newRow()]
                });
            }, 100);

            form.on('radio(portalType)', function (opt) {
                portalType = opt.value;
                reloadPortalName(portalType);
            });

            $('#btnRefreshPortal').click(function () {
                reloadPortalName();
            });

            function reloadPortalName() {
                admin.post('do-list-portal-names', {'portalType': portalType}, function (result) {
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

            function newRow() {
                refreshServiceNames();
                count++;
                return {
                    'index': count,
                    'serviceName': '',
                    'content': '',
                    'contents': [],
                    'uuid': '',
                    'address': '',
                    'serviceNameList': serviceNameList
                };
            }

            form.on('select(serviceName)', function (obj) {
                const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                const gd = table.cache['grid'];
                const serviceName = obj.value;

                admin.post('do-list-service-metadata', {'serviceName': serviceName}, function (result) {
                    const contents = [];
                    $.each(result.data, function (i, item) {
                        contents.push({
                            'uuid': item.metadata.spring_application_uuid,
                            'host': item.host,
                            'port': item.port
                        });
                    });
                    gd[dataIndex]['serviceName'] = serviceName;
                    gd[dataIndex]['uuid'] = '';
                    gd[dataIndex]['contents'] = contents;
                    reload(gd);
                });
            });

            form.on('select(content)', function (obj) {
                const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                const gd = table.cache['grid'];
                gd[dataIndex]['uuid'] = obj.value;
                gd[dataIndex]['address'] = $('option[value="' + obj.value + '"]').attr('address');
                reload(gd);
            });

            table.on('tool(grid)', function (obj) {
                const gd = table.cache['grid'];
                if (obj.event === 'add') {
                    gd.push(newRow());
                    reload(gd);
                    $('div[class="layui-table-mend"]').remove();
                } else if (obj.event === 'remove') {
                    if (gd.length > 1) {
                        $.each(gd, function (i, item) {
                            if (item && item.index == obj.data.index) {
                                gd.remove(item);
                            }
                        });
                        reload(gd);
                        $('div[class="layui-table-mend"]').remove();
                    }
                } else if (obj.event === 'refresh') {
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

                    admin.post('do-list-service-metadata', {'serviceName': serviceName}, function (result) {
                        const contents = [];
                        $.each(result.data, function (i, item) {
                            contents.push({
                                'uuid': item.metadata.spring_application_uuid,
                                'host': item.host,
                                'port': item.port
                            });
                        });

                        $.each(gd, function (index, item) {
                            if (item.index == obj.data.index) {
                                item['serviceName'] = serviceName;
                                item['uuid'] = '';
                                item['contents'] = contents;
                                return;
                            }
                        });

                        reload(gd);
                    });
                }
            });

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

            function reload(data) {
                $.each(data, function (i, d) {
                    d.index = i;
                });
                table.reload('grid', {'data': data});
            }

            $('#callback').click(function () {
                const r = [];
                const duplicates = new Set();
                const gd = table.cache['grid'];
                $.each(gd, function (index, item) {
                    if (item.serviceName != '' &&
                        (item.uuid != '' || item.address != '')) {
                        const d = {
                            'serviceName': item.serviceName,
                            'uuid': item.uuid,
                            'address': item.address
                        };
                        if (!duplicates.has(JSON.stringify(d))) {
                            r.push(d);
                            duplicates.add(JSON.stringify(d));
                        }
                    }
                });
                $('#serviceBlacklist').val(JSON.stringify(r));
            });
        });
    </script>
    </body>
    </html>
</@compress>