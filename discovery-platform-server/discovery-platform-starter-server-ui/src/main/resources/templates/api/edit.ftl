<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
    </head>
    <body>

    <div class="layui-form" lay-filter="layuiadmin-form-admin" id="layuiadmin-form-admin"
         style="padding: 20px 30px 0 0;">

        <div class="layui-form-item">
            <label class="layui-form-label">接口名称</label>
            <div class="layui-input-inline">
                <input type="text" name="name" lay-verify="required" class="layui-input" style="width: 740px"
                       placeholder="请输入接口名称" autocomplete="off" value="${api.name}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">请求方式</label>
            <div class="layui-input-inline">
                <input type="text" name="method" lay-verify="required" class="layui-input" style="width: 740px"
                       placeholder="请输入请求方式（多个用逗号分隔）" autocomplete="off" value="${api.method}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">服务名称</label>
            <div class="layui-input-inline" style="width: 740px">
                <select name="serviceName" autocomplete="off" lay-verify="required" class="layui-select" lay-search>
                    <option value="">请选择服务名称</option>
                    <#list serviceNames as serviceName>
                        <option value="${serviceName}"
                                <#if api.serviceName==serviceName>selected="selected"</#if>>${serviceName}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">请求路径</label>
            <div class="layui-input-inline">
                <input type="text" name="path" lay-verify="required" class="layui-input" style="width: 740px"
                       placeholder="请输入请求路径" autocomplete="off" value="${api.path}">
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {

        });
    </script>
    </body>
    </html>
</@compress>