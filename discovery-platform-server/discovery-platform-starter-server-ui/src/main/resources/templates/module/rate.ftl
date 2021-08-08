<#macro rate gridId="grid">
    <span class="layui-badge layui-bg-blue">流量配比</span>
    <table class="layui-hide" id="${gridId}$_INDEX_$" lay-filter="${gridId}$_INDEX_$"></table>

    <script type="text/html" id="templateRouteId$_INDEX_$">
        <select name='grayRouteId$_INDEX_$' lay-filter='grayRouteId$_INDEX_$' lay-search>
            <option value="">请选择链路标识</option>
            {{# layui.each(d.routeIdList, function(index, item){ }}
            <option value="{{ item.routeId }}" {{ d.routeId==item.routeId ?
            'selected="selected"' : '' }}>
            {{# if(item.description == ''){ }}
            {{ item.routeId }}
            {{# } else { }}
            {{ item.routeId }} ({{ item.description}})
            {{# } }}
            </option>)
            {{# }); }}
        </select>
    </script>

    <script type="text/html" id="grid-route-bar">
        <@update>
            <div class="layui-btn-group">
                <a class="layui-btn layui-btn-sm" lay-event="refreshRoute">
                    <i class="layui-icon">&#xe669;</i>
                </a>
                <a class="layui-btn layui-btn-sm" lay-event="addRoute">
                    <i class="layui-icon">&#xe654;</i>
                </a>
                <a class="layui-btn layui-btn-warm layui-btn-sm" lay-event="removeRoute">
                    <i class="layui-icon">&#xe67e;</i>
                </a>
            </div>
        </@update>
    </script>
</#macro>