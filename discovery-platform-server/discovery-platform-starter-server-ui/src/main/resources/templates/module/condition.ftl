<#macro condition gridId="grid">
    <span class="layui-badge layui-bg-blue">条件设置</span>
    <table class="layui-hide" id="${gridId}$_INDEX_$" lay-filter="${gridId}$_INDEX_$"></table>

    <script type="text/html" id="tOperator$_INDEX_$">
        <select name='operator' lay-filter='operator' tag="$_INDEX_$">
            <#list operators as operator>
                <option value="${operator.value}" {{ d.operator=='${operator.value}' ?'selected="selected"' : '' }}>
                ${operator.value}
                </option>
            </#list>
        </select>
    </script>

    <script type="text/html" id="tLogic$_INDEX_$">
        <select name='logic' lay-filter='logic' tag="$_INDEX_$">
            <#list logics as logic>
                <option value="${logic.value}" {{ d.logic=='${logic.value}' ?'selected="selected"' : '' }}>
                ${logic.value}
                </option>
            </#list>
        </select>
    </script>

    <script type="text/html" id="grid-condition-bar">
        <@update>
            <div class="layui-btn-group">
                <a class="layui-btn layui-btn-sm" lay-event="addCondition">
                    <i class="layui-icon">&#xe654;</i></a>
                <a class="layui-btn layui-btn-warm layui-btn-sm" lay-event="removeCondition">
                    <i class="layui-icon">&#xe67e;</i></a>
            </div>
        </@update>
    </script>

    <div class="layui-row">
        <div class="layui-col-md9">
            <input type="text" id="spelCondition$_INDEX_$" class="layui-input" placeholder="聚合条件表达式或者自定义条件表达式" autocomplete="off">
        </div>
        <div class="layui-col-md3" style="text-align:center;margin-top: 3px;">
            <div class="layui-btn-group">
                <button class="layui-btn layui-btn-sm" id="btnAssemble$_INDEX_$" tag="$_INDEX_$">
                    <i class="layui-icon">&#xe674;</i>&nbsp;聚合条件
                </button>
                <button class="layui-btn layui-btn-sm layui-btn-normal" id="btnVerify$_INDEX_$" tag="$_INDEX_$">
                    <i class="layui-icon">&#x1005;</i>&nbsp;校验条件
                </button>
            </div>
        </div>
    </div>
</#macro>