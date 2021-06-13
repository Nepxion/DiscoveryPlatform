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
            <label class="layui-form-label">网关名称</label>
            <div class="layui-input-inline" style="width: 800px">
                <select id="gatewayName" name="gatewayName" lay-filter="gatewayName" lay-verify="required" lay-search>
                    <option value="">请选择网关名称</option>
                    <#list gatewayNames as gatewayName>
                        <option value="${gatewayName}">${gatewayName}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">描述信息</label>
            <div class="layui-input-inline">
                <input type="text" id="description" name="description" class="layui-input" style="width: 800px"
                       placeholder="请输入该条黑名单的描述信息" autocomplete="off">
                <input type="hidden" id="serviceUUID" name="serviceUUID"/>
                <input type="hidden" id="serviceAddress" name="serviceAddress"/>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label" style="width: 90px"></label>
            <div class="layui-input-inline">
                <input type="radio" name="type" value="uuid" title="UUID黑名单" checked>
                <input type="radio" name="type" value="address" title="IP地址和端口黑名单">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">黑名单</label>
            <div class="layui-input-inline" style="width: 800px;margin-top:-12px">
                <div class="layui-tab layui-tab-brief">
                    <ul class="layui-tab-title">
                        <li class="layui-this">全局唯一ID黑名单</li>
                        <li>IP地址和端口黑名单</li>
                    </ul>
                    <div class="layui-tab-content">
                        <div id="uuid" class="layui-tab-item layui-show">

                        </div>

                        <div id="address" class="layui-tab-item">

                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>

    <input id="callback" type="button" style="display: none"/>
    <div id="template" style="display: none">
        <div id="row_$_INDEX_$" class="layui-form-item row">
            <label name="uuidServiceTitle" class="layui-form-label" style="width: 50px">服务: </label>
            <div class="layui-input-inline" style="width: 280px">
                <select lay-filter="serviceName_$_INDEX_$" class="layui-select" lay-search>
                    <option value="">请选择服务名称</option>
                    <#list serviceNames as serviceName>
                        <option value="${serviceName}">${serviceName}</option>
                    </#list>
                </select>
            </div>
            <div class="layui-input-inline" style="width: 290px">
                <select id="serviceUUID_$_INDEX_$" class="layui-select">
                </select>
            </div>
            <div class="layui-input-inline" style="width: 100px;margin-top: 3px">
                <button id="add_$_INDEX_$" type="button" class="btnAdd layui-btn layui-btn-sm" title="添加">
                    <i class="layui-icon">&#xe654;</i>
                </button>
                <button id="del_$_INDEX_$" tag="$_INDEX_$" type="button" class="btnDelete layui-btn layui-btn-sm layui-btn-warm" title="删除">
                    <i class="layui-icon">&#xe640;</i>
                </button>
            </div>
        </div>
    </div>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            const admin = layui.admin, form = layui.form, $ = layui.$;
            let uuidIndex = 1, addressIndex = 1;

            function addRowForUUID(index) {
                index = "uuid_" + index;
                let template = $('#template').html();
                template = template.replaceAll('$_INDEX_$', index);
                $('#uuid').append(template);

                form.on('select(serviceName_' + index + ')', function (data) {
                    const serviceName = data.elem[data.elem.selectedIndex].text;
                    $('#serviceUUID_' + index).html("");
                    admin.post('do-list-service-uuid', {'serviceName': serviceName}, function (result) {
                        $.each(result.data, function (key, val) {
                            const option = $("<option>").val(val).text(val);
                            $('#serviceUUID_' + index).append(option);
                        });
                        layui.form.render('select');
                    });
                    layui.form.render('select');
                });

                $('#add_' + index).click(function () {
                    uuidIndex = uuidIndex + 1;
                    addRowForUUID(uuidIndex);
                });

                $('#del_' + index).click(function () {
                    if ($('#uuid').find('.layui-form-item').size() > 1) {
                        $('#row_' + $(this).attr('tag')).remove();
                        form.render();
                    }
                });
                form.render();
            }

            function addRowForAddress(index) {
                index = "address_" + index;
                let template = $('#template').html();
                template = template.replaceAll('$_INDEX_$', index);
                $('#address').append(template);

                form.on('select(serviceName_' + index + ')', function (data) {
                    const serviceName = data.elem[data.elem.selectedIndex].text;
                    $('#serviceUUID_' + index).html("");
                    admin.post('do-list-service-address', {'serviceName': serviceName}, function (result) {
                        $.each(result.data, function (key, val) {
                            const option = $("<option>").val(val).text(val);
                            $('#serviceUUID_' + index).append(option);
                        });
                        layui.form.render('select');
                    });
                    layui.form.render('select');
                });

                $('#add_' + index).click(function () {
                    addressIndex = addressIndex + 1;
                    addRowForAddress(addressIndex);
                });

                $('#del_' + index).click(function () {
                    if ($('#address').find('.layui-form-item').size() > 1) {
                        $('#row_' + $(this).attr('tag')).remove();
                        form.render();
                    }
                });
                form.render();
            }

            addRowForUUID(1);
            addRowForAddress(1);

            $('#callback').click(function () {
                const u = [], a = [];
                const us = new Set(), as = new Set();
                $('#uuid').find('div[class="layui-form-item row"]').each(function (i, item) {
                    const json = {};

                    $(item).find('input.layui-input').each(function (i, item) {
                        if (i == 0) {
                            json.serviceName = $(item).val();
                        } else {
                            json.uuid = $(item).val();
                        }
                    });

                    if (json.serviceName != '' && json.uuid != '') {
                        const key = json.serviceName + json.uuid;
                        if (!us.has(key)) {
                            const j = {};
                            j[json.serviceName] = json.uuid;
                            u.push(j);
                            us.add(key);
                        }
                    }
                });
                $('#serviceUUID').val(JSON.stringify(u));

                $('#address').find('div[class="layui-form-item row"]').each(function (i, item) {
                    const json = {};

                    $(item).find('input.layui-input').each(function (i, item) {
                        if (i == 0) {
                            json.serviceName = $(item).val();
                        } else {
                            json.uuid = $(item).val();
                        }
                    });

                    if (json.serviceName != '' && json.uuid != '') {
                        const key = json.serviceName + json.uuid;
                        if (!as.has(key)) {
                            const j = {};
                            j[json.serviceName] = json.uuid;
                            a.push(j);
                            as.add(key);
                        }
                    }
                });
                $('#serviceAddress').val(JSON.stringify(a));
            });

            <#if (gatewayNames?size==1) >
            chooseSelectOption('gatewayName', 1);
            </#if>
        });
    </script>
    </body>
    </html>
</@compress>