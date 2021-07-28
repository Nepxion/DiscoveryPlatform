<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
        <link rel="stylesheet" href="${ctx}/css/layui-table-select.css" media="all">
    </head>
    <body style="padding-left: 10px;padding-right: 10px">
    <table class="layui-hide" id="detail" style="margin: 0 auto"></table>
    <script id="metadataTemplate" type="text/html">
        <ul>
            {{# Object.keys(d.metadata).forEach(function (key) { }}
            <li>
                <span>{{key}}={{d.metadata[key]}}</span>
            </li>
            {{# }); }}
        </ul>
    </script>


    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
                const form = layui.form, admin = layui.admin, $ = layui.$, element = layui.element, table = layui.table;
                var strInstance = localStorage.getItem("instanceInfo");
                var instanceObj = JSON.parse(strInstance).instanceInfos;

                table.render({
                    elem: '#detail',
                    cols: [[
                        {field: 'host', title: 'IP', width: 160},
                        {field: 'port', width: 80, title: '端口'},
                        {field: 'version', width: 80, title: '版本'},
                        {field: 'zone', width: 100, title: '地区'},
                        {field: 'region', title: '区域', width: 100},
                        {field: 'metadata', title: '元数据',templet: '#metadataTemplate'}
                    ]],
                    data: instanceObj
                });
            }
        );
    </script>
    <style>
        .layui-table-cell {
            font-size: 14px;
            padding: 0 5px;
            height: auto;
            overflow: visible;
            text-overflow: inherit;
            white-space: normal;
            word-break: break-all;
        }
    </style>
    </body>
    </html>
</@compress>