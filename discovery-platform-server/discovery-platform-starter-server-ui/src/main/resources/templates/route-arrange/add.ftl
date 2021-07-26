<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
        <#include "../common/service-grid.ftl">
    </head>
    <body>

    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin"
         style="padding: 20px 30px 0 0;">
        <div class="layui-form-item">
            <label class="layui-form-label">链路标识</label>
            <div class="layui-input-inline" style="width: 1000px">
                <input type="text" id="routeId" name="routeId" readonly="readonly" class="layui-input" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">描述信息</label>
            <div class="layui-input-inline" style="width: 1000px">
                <input type="text" id="description" name="description" class="layui-input" placeholder="请输入该条链路编排的描述信息" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">策略类型</label>
            <div class="layui-input-block">
                <input type="radio" name="strategyType" value="1" title="版本" checked>
                <input type="radio" name="strategyType" value="2" title="区域">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">链路列表</label>
            <div class="layui-input-inline" style="width: 1000px">
                <@serviceGrid id="grid" metadataType="version"></@serviceGrid>
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

            setTimeout(function () {

            }, 50);

        });
    </script>
    </body>
    </html>
</@compress>