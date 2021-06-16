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
            <label class="layui-form-label">角色名称</label>
            <div class="layui-input-inline" style="width: 740px">
                <input type="text" name="name" lay-verify="required" class="layui-input"
                       autocomplete="off" value="${role.name}">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">超级管理员</label>
            <div class="layui-input-block">
                <input type="radio" name="superAdmin" value="true" title="是" ${(role.superAdmin)?string('checked','')}>
                <input type="radio" name="superAdmin" value="false"
                       title="否" ${(role.superAdmin)?string('','checked')}>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">备注</label>
            <div class="layui-input-block" style="width: 740px">
                <textarea name="description" placeholder="请输入备注" class="layui-textarea" style="resize: none">${role.description}</textarea>
            </div>
        </div>

        <div class="layui-form-item layui-hide">
            <input type="button" lay-submit lay-filter="btn_confirm" id="btn_confirm" value="确认">
        </div>
    </div>
    <script>
        layui.config({base: '../../..${ctx}/layuiadmin/'}).extend({index: 'lib/index'}).use(['index', 'form'], function () {
            // parent.layer.iframeAuto(parent.layer.getFrameIndex(window.name));
        });
    </script>
    </body>
    </html>
</@compress>