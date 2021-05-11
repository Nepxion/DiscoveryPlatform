<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
    </head>
    <body>

    <div class="layui-fluid">
        <div class="layui-card">
            <div class="layui-card-body">
                <table id="grid" lay-filter="grid"></table>
                <script type="text/html" id="grid-toolbar">
                    <div class="layui-btn-container">
                        <@select>
                            <button class="layui-btn layui-btn-sm layui-btn-primary layuiadmin-btn-admin"
                                    lay-event="refresh">
                                <i class="layui-icon layui-icon-refresh-3"></i>&nbsp;&nbsp;刷新服务列表
                            </button>
                        </@select>
                    </div>
                </script>
            </div>
        </div>
    </div>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
            const table = layui.table;
            tableErrorHandler();
            table.render({
                elem: '#grid',
                url: 'listWorking',
                toolbar: '#grid-toolbar',
                method: 'post',
                cellMinWidth: 80,
                page: false,
                limit: 99999999,
                limits: [99999999],
                even: true,
                text: {
                    none: '暂无相关数据'
                },
                cols: [[
                    {type: 'numbers', title: '序号', width: 100},
                    {field: 'data', title: '路由信息'}
                ]]
            });

            table.on('toolbar(grid)', function (obj) {
                if (obj.event === 'refresh') {
                    table.reload('grid');
                }
            });
        });
    </script>
    </body>
    </html>
</@compress>