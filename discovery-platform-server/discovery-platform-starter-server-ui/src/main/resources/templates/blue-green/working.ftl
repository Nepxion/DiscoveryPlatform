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
                <ul id="tabTitle" class="layui-tab-title">
                </ul>
                <div id="tabContent" class="layui-tab-content">
                </div>
            </div>
        </div>
    </div>

    <script src="${ctx}/js/codemirror.js"></script>
    <script src="${ctx}/js/xml.js"></script>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'table'], function () {
            const form = layui.form, $ = layui.$, element = layui.element, admin = layui.admin;
            let chooseGatewayName = '', tabIndex = -1;

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
                $("#tabTitle").html('');
                $("#tabContent").html('');

                admin.post("do-list-working", {"gatewayName": chooseGatewayName}, function (result) {
                    const data = result.data, set = new Set();
                    let index = 0;
                    $.each(data, function (k, v) {
                        set.add(JSON.stringify(v));
                        const tabTitle = k;
                        const tabId = 'tab_' + index;
                        const txtId = 'txt_' + index;
                        let showTitle = '', showContent = '';
                        if (tabIndex < 0) {
                            tabIndex = 0;
                        }
                        if (index == tabIndex) {
                            showTitle = 'class="layui-this"';
                            showContent = 'layui-show';
                        }

                        $("#tabTitle").append('<li id="' + tabId + '" ' + showTitle + '>' + tabTitle + '</li>');
                        $("#tabContent").append('<div class="layui-tab-item ' + showContent + '"><textarea id="' + txtId + '" class="layui-input" style="resize: none"></textarea></div>');
                        index++;

                        element.render();

                        const txtXml = CodeMirror.fromTextArea(document.getElementById(txtId), {
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
                        txtXml.setSize('auto', 'auto');

                        if (v == undefined || v == '' || $.trim(v) == '' || v.toString().length < 1) {
                            txtXml.setValue('');
                        } else {
                            txtXml.setValue(v);
                        }
                    });
                    element.render();

                    if (set.size <= 1) {
                        $("#tip").html('<span class="layui-badge layui-bg-blue"><h3><b>一致性检查</b>:&nbsp;&nbsp;所有网关的蓝绿配置信息一致&nbsp;</h3></span>');
                    } else {
                        $("#tip").html('<span class="layui-badge layui-bg-orange"><h3><b>一致性检查</b>:&nbsp;&nbsp;有网关的蓝绿配置信息不一致, 请检查&nbsp;</h3></span>');
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