<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
    </head>
    <body>

    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin"
         style="padding: 20px 30px 0 0;">

        <div class="layui-form layui-card-header layuiadmin-card-header-auto">
            <div class="layui-form-item">
                <div class="layui-inline">服务名称</div>
                <div class="layui-inline" style="width:260px">
                    <select name="serviceName" autocomplete="off" class="layui-select" lay-search>
                        <option value="">请选择服务名称</option>
                        <#list serviceNames as serviceName>
                            <option value="${serviceName}">${serviceName}</option>
                        </#list>
                    </select>

                </div>

                <div class="layui-inline">接口名称</div>
                <div class="layui-inline" style="width:260px">
                    <input type="text" name="name" class="layui-input" placeholder="请输入接口名称"
                           autocomplete="off">
                </div>

                <div class="layui-inline">请求路径</div>
                <div class="layui-inline" style="width:260px">
                    <input type="text" name="path" class="layui-input" placeholder="请输入请求路径"
                           autocomplete="off">
                </div>

                <input type="hidden" name="appId" value="${appId}">
                <input type="hidden" id="apiIds" name="apiIds">

                <div class="layui-inline">
                    <button id="search" class="layui-btn layuiadmin-btn-admin" lay-submit lay-filter="search">
                        <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                    </button>
                </div>
            </div>
        </div>

        <div class="layui-card-body">
            <table id="grid" lay-filter="grid"></table>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table', 'form'], function () {
            const $ = layui.$, admin = layui.admin, form = layui.form, table = layui.table;
            let ids = new Array(), pageIds = new Array();
            tableErrorHandler();
            form.on('submit(search)', function (data) {
                const field = data.field;
                table.reload('grid', {page: {curr: 1}, where: field});
            });
            table.render({
                elem: '#grid',
                url: 'listApis?appId=${appId}',
                method: 'post',
                cellMinWidth: 80,
                defaultToolbar: [],
                page: true,
                limit: 15,
                limits: [15],
                even: true,
                text: {
                    none: '暂无相关数据'
                },
                cols: [[
                    {type: 'checkbox'},
                    {type: 'numbers', title: '序号', width: 50},
                    {field: 'serviceName', title: '服务名称', width: 250},
                    {field: 'name', title: '接口名称', width: 300},
                    {field: 'method', title: '请求方式', width: 200},
                    {field: 'path', title: '接口路径'}
                ]],
                done: function (res) {
                    pageIds = res.data.map(function (value) {
                        return value.id;
                    });

                    $.each(res.data, function (idx, val) {
                        if (ids.indexOf(val.id) > -1) {
                            val["LAY_CHECKED"] = 'true';
                            let index = val['LAY_TABLE_INDEX'];
                            $('tr[data-index=' + index + '] input[type="checkbox"]').click();
                            form.render('checkbox');
                        }
                    });

                    let checkStatus = table.checkStatus('grid');
                    if (checkStatus.isAll) {
                        $('.layui-table-header th[data-field="0"] input[type="checkbox"]').prop('checked', true);
                        form.render('checkbox');
                    }
                }
            });

            table.on('checkbox(grid)', function (obj) {
                if (obj.checked == true) {
                    if (obj.type == 'one') {
                        ids.push(obj.data.id);
                    } else {
                        for (let i = 0; i < pageIds.length; i++) {
                            if (ids.indexOf(pageIds[i]) == -1) {
                                ids.push(pageIds[i]);
                            }
                        }
                    }
                } else {
                    if (obj.type == 'one') {
                        let i = ids.length;
                        while (i--) {
                            if (ids[i] == obj.data.id) {
                                ids.splice(i, 1);
                            }
                        }
                    } else {
                        let i = ids.length;
                        while (i--) {
                            if (pageIds.indexOf(ids[i]) != -1) {
                                ids.splice(i, 1);
                            }
                        }
                    }
                }
                $("#apiIds").val(ids.join(","));
            });
        });
    </script>
    </body>
    </html>
</@compress>