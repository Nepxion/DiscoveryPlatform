<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
        <link rel="stylesheet" href="${ctx}/css/codemirror.css" media="all">
        <link rel="stylesheet" href="${ctx}/css/idea.css" media="all">
    </head>
    <body>

    <div class="layui-fluid">
        <div class="layui-card">
            <div class="layui-form layui-card-header layuiadmin-card-header-auto">
                <div class="layui-form-item">
                    <div class="layui-inline">网关列表</div>
                    <div class="layui-inline" style="width:350px">
                        <select id="gatewayName" name="gatewayName" lay-filter="gatewayName" autocomplete="off"
                                lay-verify="required" class="layui-select" lay-search>
                            <option value="">请选择网关名称</option>
                            <#list gatewayNames as gatewayName>
                                <option value="${gatewayName}">${gatewayName}</option>
                            </#list>
                        </select>
                    </div>

                    <div class="layui-inline" style="width:120px">
                        <button id="btnRefreshGateway" class="layui-btn">
                            刷新网关列表
                        </button>
                    </div>

                    <div class="layui-inline"></div>
                    <div id="tip" class="layui-inline" style="width:350px">
                    </div>

                </div>
            </div>

            <div class="layui-card-body" id="content">
                <textarea id="txtXml" class="layui-input" autocomplete="off" style="resize: none"></textarea>
            </div>
        </div>
    </div>

    <script src="${ctx}/js/codemirror.js"></script>
    <script src="${ctx}/js/xml.js"></script>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
            const form = layui.form, $ = layui.$, admin = layui.admin;
            let chooseGatewayName = '';

            const txtXml = CodeMirror.fromTextArea(document.getElementById("txtXml"), {
                readOnly: "no",
                lineNumbers: true,
                theme: 'idea',
                indentUnit: 4,
                mode: "text/xml",
                matchBrackets: true,
                theme: "idea",
                styleActiveLine: true,
                autoRefresh: true
            });
            txtXml.setSize('auto', '435px');

            form.on('select(gatewayName)', function (data) {
                chooseGatewayName = data.value;
                reloadBlueGreenContent();
            });

            $("#btnRefreshGateway").click(function () {
                admin.post("do-list-gateway-names", {}, function (data) {
                    data = data.data;
                    const selGatewayName = $("select[name=gatewayName]");
                    selGatewayName.html('<option value="">请选择网关名称</option>');
                    $.each(data, function (key, val) {
                        let option;
                        if (chooseGatewayName == val) {
                            option = $("<option>").attr('selected', 'selected').val(val).text(val);
                            chooseGatewayName = val;
                        } else {
                            option = $("<option>").val(val).text(val);
                        }
                        selGatewayName.append(option);
                    });
                    layui.form.render('select');
                    reloadBlueGreenContent();
                });
            });

            function reloadBlueGreenContent() {
                admin.post("do-list-working", {"gatewayName": chooseGatewayName}, function (result) {
                    const data = result.data;
                    if (data == undefined || data == '' || $.trim(data) == '' || data.toString().length < 1) {
                        txtXml.setValue('');
                    } else {
                        txtXml.setValue(data);
                    }
                });
            }

            <#if (gatewayNames?size==1) >
            chooseSelectOption('gatewayName', 1);
            </#if>
        });
    </script>
    </body>
    </html>
</@compress>