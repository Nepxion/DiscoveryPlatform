<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
        <style type="text/css">
            .layui-table-cell {
                overflow: visible !important;
            }

            td .layui-form-select {
                margin-top: -10px;
                margin-left: -15px;
                margin-right: -15px;
            }

            .layui-form-select dl {
                z-index: 9999;
            }

            .layui-table-cell {
                overflow: visible;
            }

            .layui-table-box {
                overflow: visible;
            }

            .layui-table-body {
                overflow: visible;
            }

            .div-inline {
                display: inline
            }
        </style>
    </head>
    <body>

    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin"
         style="padding: 5px 5px 5px 5px;">

        <div class="layui-form-item" style="margin-top: 20px">
            <div class="layui-inline" style="margin-left: 50px">流量比例</div>
            <div class="layui-inline" style="width:300px">
                <div id="weightSlider" class="demo-slider"></div>
            </div>
            <div class="layui-inline" style="margin-left: -10px"><b>%</b></div>
            <input type="hidden" id="weight" name="weight" value="${gray.weight}">
            <input type="hidden" name="grayId" value="${gray.grayId}">

            <div class="layui-inline" style="margin-left: 30px">描述信息</div>
            <div class="layui-inline" style="width:300px">
                <input type="text" name="description" class="layui-input" placeholder="请填写描述信息" autocomplete="off"
                       lay-verify="required" value="${gray.description}">
            </div>
        </div>

        <hr/>

        <table id="grid" lay-filter="grid"></table>

        <script type="text/html" id="grid-bar">
            <@delete>
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i
                            class="layui-icon layui-icon-delete"></i>删除</a>
            </@delete>
        </script>

        <script type="text/html" id="grid-toolbar">
            <div class="layui-btn-container">
                <@update>
                    <button class="layui-btn layui-btn-primary layui-btn-sm layuiadmin-btn-admin" lay-event="inspect">
                        <i class="layui-icon layui-icon-rss"></i>&nbsp;&nbsp;测试调用链路
                    </button>
                </@update>
                <@select>
                    <button class="layui-btn layui-btn-primary layui-btn-sm layuiadmin-btn-admin" lay-event="refresh">
                        <i class="layui-icon layui-icon-refresh-3"></i>&nbsp;&nbsp;刷新服务列表
                    </button>
                </@select>

            </div>
        </script>

        <script type="text/html" id="tName">
            {{#  if(d.versionList.length > 0){ }}
            <span class="layui-badge layui-bg-green">{{ d.name }}</span>
            {{#  } else { }}
            <span class="layui-badge layui-bg-orange" style="cursor:pointer" title="服务{{ d.name }}没有配置version">{{ d.name }}</span>
            {{#  } }}
        </script>

        <script type="text/html" id="tVersion">
            {{#  if(d.versionList.length > 0){ }}
            <select name='version' lay-filter='version' lay-search>
                <option value="">请选择版本号</option>
                {{# layui.each(d.versionList, function(index, item){ }}
                <option value="{{item}}" {{d.version==item?'selected="selected"':''}}>
                    {{item}}
                </option>
                {{# }); }}
            </select>
            {{#  } }}
        </script>

        <script type="text/html" id="tVersionSize">
            {{#  if(d.versionList.length > 0){ }}
            <span class="layui-badge layui-bg-green">{{ d.versionList.length }}</span>
            {{#  } else { }}
            <span class="layui-badge layui-bg-orange" style="cursor:pointer" title="服务{{ d.name }}没有配置version">{{ d.versionList.length }}</span>
            {{#  } }}
        </script>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'slider', 'table', 'form'], function () {
            const table = layui.table, form = layui.form, $ = layui.$, slider = layui.slider, admin = layui.admin;

            slider.render({
                elem: '#weightSlider',
                min: 0,
                max: 100,
                step: 1,
                value: ${gray.weight},
                input: true,
                setTips: function (value) {
                    return value + '%';
                },
                change: function (value) {
                    $('#weight').val(value.replace(/%/, ''));
                }
            });

            function reload(data) {
                table.reload('grid', {url: null, 'data': data});
            }

            tableErrorHandler();
            table.render({
                elem: '#grid',
                url: 'listServices?value=' + escape('${gray.value}'),
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
                    {title: '服务ID', templet: '#tName'},
                    {title: '版本号', unresize: true, templet: '#tVersion', width: 250},
                    {title: '版本数量', align: "center", templet: '#tVersionSize', width: 150},
                    {fixed: 'right', title: '操作', align: "center", toolbar: '#grid-bar', width: 80}
                ]],
                done: function (res) {

                }
            });

            table.on('tool(grid)', function (obj) {
                const data = obj.data;
                if (obj.event === 'del') {
                    const d = table.cache['grid'];
                    $.each(d, function (index, item) {
                        if (item) {
                            if (item.name === data.name) {
                                d.remove(item);
                                return;
                            }
                        }
                    });
                    reload(d);
                }
            });

            table.on('toolbar(grid)', function (obj) {
                if (obj.event === 'refresh') {
                    table.reload('grid', {url: 'listServices?value=' + escape('${gray.value}')});
                } else if (obj.event === 'inspect') {
                    const data = table.cache['grid'];
                    const service = [];
                    $.each(data, function (index, item) {
                        if (item.version) {
                            service.push({
                                "service": item.name,
                                "version": item.version
                            });
                        }
                    });

                    if (service.length < 1) {
                        admin.error("系统提示", "请先选择服务对应的版本");
                        return;
                    }

                    layer.open({
                        type: 2,
                        title: '<i class="layui-icon layui-icon-rss" style="color: #1E9FFF;"></i>&nbsp;测试调用链路',
                        content: 'toinspect?data=' + escape(JSON.stringify(service)),
                        area: ['80%', '100%'],
                        btn: ['取消'],
                        closeBtn: 0,
                        resize: false
                    });
                }
            });

            form.on('select(version)', function (obj) {
                const dataIndex = $(obj.elem).parent().parent().parent().attr('data-index');
                const data = table.cache['grid'];
                data[dataIndex]['version'] = obj.value;
                reload(data);
            });

            setTimeout(function () {
                $("#weight").select();
                $("#weight").focus();
            }, 100);
        });
    </script>
    </body>
    </html>
</@compress>