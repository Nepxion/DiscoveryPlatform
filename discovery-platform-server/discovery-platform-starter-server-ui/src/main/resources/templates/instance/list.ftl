<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
    </head>
    <body>

    <div class="layui-fluid">
        <div class="layui-card">
            <div class="layui-form layui-card-header layuiadmin-card-header-auto">
                <div class="layui-form-item">
                    <div class="layui-inline" style="width:500px">
                        <input type="text" name="name" placeholder="请输入服务名称" autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-inline">
                        <button id="search" class="layui-btn layuiadmin-btn-admin" lay-submit lay-filter="search">
                            <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
                        </button>
                    </div>
                </div>
            </div>

            <div class="layui-card-body">
                <table id="grid" lay-filter="grid"></table>
                <script type="text/html" id="grid-bar">
                    <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="detail"><i
                                class="layui-icon layui-icon-edit"></i>详情</a>
                </script>
            </div>
        </div>
    </div>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
            const admin = layui.admin, $ = layui.$, form = layui.form, table = layui.table;
            tableErrorHandler();
            form.on('submit(search)', function (data) {
                const field = data.field;
                table.reload('grid', {where: field});
            });
            table.render({
                elem: '#grid',
                url: 'do-list',
                toolbar: '#grid-toolbar',
                method: 'post',
                cellMinWidth: 80,
                page: true,
                limit: 20,
                limits: [99999999],
                even: true,
                text: {
                    none: '暂无相关数据'
                },
                cols: [[
                    {type: 'numbers', title: '序号', width: 50},
                    {field: 'serviceId', title: '服务名', align: 'center'},
                    {field: 'serviceType', title: '服务类型', width: 300},
                    {field: 'instanceNum', title: '实例数', align: 'center', sort: true, width: 150},
                    {fixed: 'right', title: '操作', align: 'center', toolbar: '#grid-bar', width: 150}
                ]],
                done: function (res) {
                    $.each(res.data, function (idx, val) {
                        if (val.serviceType == null) {
                            res.data[idx].serviceType = "未定义";
                        }
                    });
                }
            });
            table.on('tool(grid)', function (obj) {
                var data = JSON.stringify(obj.data);
                localStorage.setItem("instanceInfo", data);
                toAddPage(data);
            });

            function toAddPage(data) {
                layer.open({
                    type: 2,
                    title: '<i class="layui-icon layui-icon-read"></i>&nbsp;' + "实例详情",
                    content: 'detail',
                    area: ['80%', '80%'],
                    btn: "",
                    resize: false,
                    closeBtn: 1,
                    shadeClose:true
                });
            }
        });
    </script>
    </body>
    </html>
</@compress>