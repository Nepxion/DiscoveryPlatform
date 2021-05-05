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
            <label class="layui-form-label">路由描述</label>
            <div class="layui-input-inline">
                <input type="text" id="description" name="description" lay-verify="required" class="layui-input" style="width: 740px"
                       placeholder="请输入路由描述" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">服务名称</label>
            <div class="layui-input-inline">
                <input type="text" id="serviceName" name="serviceName" lay-verify="required" class="layui-input"
                       style="width: 740px"
                       placeholder="请输入服务名称(即: 服务的spring.application.name值)" autocomplete="off">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">目标地址</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" id="uri" name="uri" class="layui-input" placeholder="请输入目标地址"
                       style="position:absolute;z-index:2;width:96%;" lay-verify="required" autocomplete="off">
                <select name="uri1" lay-filter="uri1" autocomplete="off" lay-verify="required"
                        class="layui-select" lay-search>
                    <#list serviceNames as serviceName>
                        <option value="lb://${serviceName}" tag="${serviceName}">lb://${serviceName}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">断言器</label>
            <div class="layui-input-inline">
                <textarea id="predicates" name="predicates" class="layui-input" autocomplete="off"
                          placeholder="请输入断言字符串"
                          style="width: 740px;height:140px;resize: none"></textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">过滤器</label>
            <div class="layui-input-inline">
                <textarea id="filters" name="filters" class="layui-input" autocomplete="off" placeholder="请输入过滤字符串"
                          style="width: 740px;height:140px;resize: none"></textarea>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">执行顺序</label>
            <div class="layui-input-inline">
                <input type="number" name="orderNum" lay-verify="required" class="layui-input" style="width: 740px"
                       placeholder="请输入顺序号" autocomplete="off" value="0">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">是否启用</label>
            <div class="layui-input-block">
                <input type="radio" name="enabled" value="true" title="启用" checked>
                <input type="radio" name="enabled" value="false" title="禁用">
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const form = layui.form, $ = layui.$;

            form.on('select(uri1)', function (data) {
                const text = data.elem[data.elem.selectedIndex].text;
                const name = $(data.elem[data.elem.selectedIndex]).attr('tag');
                $("#uri").val(text);
                $("#serviceName").val(name);
                $("#predicates").val('Path=/' + name + '/**');
                $("#filters").val('StripPrefix=1');
                $("#uri1").next().find("dl").css({"display": "none"});
                form.render();
                $("#description").focus();
            });

            $('#uri').bind('input propertychange', function () {
                const value = $("#uri").val();
                $("#uri1").val(value);
                form.render();
                $("#uri1").next().find("dl").css({"display": "block"});
                const dl = $("#uri1").next().find("dl").children();
                let j = -1;
                for (let i = 0; i < dl.length; i++) {
                    if (dl[i].innerHTML.indexOf(value) <= -1) {
                        dl[i].style.display = "none";
                        j++;
                    }
                    if (j == dl.length - 1) {
                        $("#uri1").next().find("dl").css({"display": "none"});
                    }
                }
            });
        });
    </script>
    </body>
    </html>
</@compress>