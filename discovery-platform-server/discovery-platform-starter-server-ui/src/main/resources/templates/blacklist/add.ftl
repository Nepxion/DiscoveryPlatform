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
            <label class="layui-form-label">入口名称</label>
            <div class="layui-input-inline" style="width: 1000px">
                <select id="gatewayName" name="gatewayName" lay-filter="gatewayName" lay-verify="required" lay-search>
                    <option value="">请选择网关或者服务名称</option>
                    <#list gatewayNames as gatewayName>
                        <option value="${gatewayName}">${gatewayName}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">描述信息</label>
            <div class="layui-input-inline">
                <input type="text" id="description" name="description" class="layui-input" style="width: 1000px"
                       placeholder="请输入该条黑名单的描述信息" autocomplete="off">
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
                        <#list serviceNames as serviceName>
                            <option value="${serviceName}" {{ d.serviceName=='${serviceName}' ?'selected="selected"' : '' }}>${serviceName}</option>
                        </#list>
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
                        <a class="layui-btn layui-btn-sm" lay-event="add">
                            <i class="layui-icon">&#xe654;</i></a>
                        <a class="layui-btn layui-btn-warm layui-btn-sm" lay-event="remove">
                            <i class="layui-icon">&#xe67e;</i></a>
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
            let count = 0;
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
                    {field: 'uuid', title: '服务实例 [UUD&nbsp;|&nbsp;IP地址:端口]', unresize: true, templet: '#tContent', width: 460},
                    {title: '操作', align: 'center', toolbar: '#grid-bar', width: 130}
                ]],
                data: [newRow()]
            });

            function newRow() {
                count++;
                return {
                    'index': count,
                    'serviceName': '',
                    'content': '',
                    'contents': [],
                    'uuid': '',
                    'address': ''
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
                }
            });

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

            <#if (gatewayNames?size==1) >
            chooseSelectOption('gatewayName', 1);
            </#if>
        });
    </script>
    </body>
    </html>
</@compress>