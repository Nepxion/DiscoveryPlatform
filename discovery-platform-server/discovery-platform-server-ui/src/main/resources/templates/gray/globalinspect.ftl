<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <#include "../common/layui.ftl">
    </head>

    <style type="text/css">
        body {
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }
    </style>
    <body>

    <div class="layui-form" style="padding: 10px 30px 0 30px;">

        <div class="layui-tab layui-tab-brief" lay-filter="form">
            <div class="layui-form-item">
                <div class="layui-inline">调用次数</div>
                <div class="layui-inline" style="width:150px">
                    <input type="number" id="times" name="times" class="layui-input" placeholder="请填写调用次数"
                           autocomplete="off" value="10">
                </div>

                <button id="btnStart" type="button" class="layui-btn layui-btn-radius"
                        style="height: 38px;margin-top: -5px;">&nbsp;&nbsp;开&nbsp;&nbsp;始&nbsp;&nbsp;
                </button>
                <button id="btnStop" type="button" class="layui-btn layui-btn-radius layui-btn-disabled"
                        style="height: 38px;margin-top: -5px;">&nbsp;&nbsp;停&nbsp;&nbsp;止&nbsp;&nbsp;
                </button>
            </div>
        </div>

        <blockquote id="message" class="layui-elem-quote">
        </blockquote>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
            <input id="complete" name="complete"/>
        </div>
    </div>

    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'element', 'form'], function () {
            const admin = layui.admin, $ = layui.$;
            let content = [], interval = null, times = 0;

            function refresh() {
                admin.postQuiet('globalinspect', {}, function (data) {
                    content.push(data.data);
                    $("#message").html(content.join("<br/><br/>"));
                    times++;
                    if (times >= parseInt($("#times").val())) {
                        $("#btnStop").click();
                    }
                });
            }

            $("#btnStart").click(function () {
                if ($("#btnStart").hasClass("layui-btn-disabled")) {
                    return;
                }
                $("#message").html("");
                const times = parseInt($("#times").val());
                if (times > 0 && times <= 100) {
                    interval = setInterval(refresh, 1000);
                    $("#btnStart").addClass("layui-btn-disabled");
                    $("#btnStop").removeClass("layui-btn-disabled");
                } else {
                    admin.error("系统提示", "调用次数范围必须在0~100之间")
                }
            });

            $("#btnStop").click(function () {
                if ($("#btnStop").hasClass("layui-btn-disabled")) {
                    return;
                }
                clearInterval(interval);
                interval = null;
                content = [];
                times = 0;
                $("#btnStart").removeClass("layui-btn-disabled");
                $("#btnStop").addClass("layui-btn-disabled");
            });
        });
    </script>
    </body>
    </html>
</@compress>