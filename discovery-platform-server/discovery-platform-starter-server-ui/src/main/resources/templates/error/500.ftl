<#assign ctx=request.contextPath>
<@compress single_line=true>
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <meta charset="utf-8">
        <title>出错了</title>
        <meta name="renderer" content="webkit">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport"
              content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
        <link rel="stylesheet" href="${ctx}/layuiadmin/layui/css/layui.css" media="all">
        <link rel="stylesheet" href="${ctx}/layuiadmin/style/admin.css" media="all">
    </head>
    <body bgcolor="#ffffff">

    <div class="layui-fluid">
        <div class="layadmin-tips">
            <i class="layui-icon" face>&#xe664;</i>

            <div class="layui-text" style="font-size: 20px;color:red">
                好像出错了呢
            </div>

            <div style="text-align:left;font-size: 20px">
                ${error}
            </div>

        </div>
    </div>

    <script src="${ctx}/layuiadmin/layui/layui.js"></script>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index']);
    </script>
    </body>
    </html>
</@compress>