<#ftl output_format="HTML" strip_whitespace=true>

<#macro message code>${springMacroRequestContext.getMessage(code)?no_esc}</#macro>

<#macro messageText code, text>${springMacroRequestContext.getMessage(code, text)?no_esc}</#macro>

<#macro messageArgs code, args>${springMacroRequestContext.getMessage(code, args)?no_esc}</#macro>

<#macro messageArgsText code, args, text>${springMacroRequestContext.getMessage(code, args, text)?no_esc}</#macro>

<#macro theme code>${springMacroRequestContext.getThemeMessage(code)?no_esc}</#macro>

<#macro themeText code, text>${springMacroRequestContext.getThemeMessage(code, text)?no_esc}</#macro>

<#macro themeArgs code, args>${springMacroRequestContext.getThemeMessage(code, args)?no_esc}</#macro>

<#macro themeArgsText code, args, text>${springMacroRequestContext.getThemeMessage(code, args, text)?no_esc}</#macro>

<#macro url relativeUrl extra...><#if extra?? && extra?size!=0>${springMacroRequestContext.getContextUrl(relativeUrl,extra)?no_esc}<#else>${springMacroRequestContext.getContextUrl(relativeUrl)?no_esc}</#if></#macro>

<#macro bind path>
    <#if htmlEscape??>
        <#assign status = springMacroRequestContext.getBindStatus(path, htmlEscape)>
    <#else>
        <#assign status = springMacroRequestContext.getBindStatus(path)>
    </#if>

    <#if status.value?? && status.value?is_boolean>
        <#assign stringStatusValue=status.value?string>
    <#else>
        <#assign stringStatusValue=status.value!"">
    </#if>
</#macro>

<#macro bindEscaped path, htmlEscape>
    <#assign status = springMacroRequestContext.getBindStatus(path, htmlEscape)>
    <#if status.value?? && status.value?is_boolean>
        <#assign stringStatusValue=status.value?string>
    <#else>
        <#assign stringStatusValue=status.value!"">
    </#if>
</#macro>

<#macro formInput path attributes="" fieldType="text">
    <@bind path/>
    <input type="${fieldType}" id="${status.expression?replace('[','')?replace(']','')}" name="${status.expression}"
           value="<#if fieldType!="password">${stringStatusValue}</#if>" ${attributes?no_esc}<@closeTag/>
</#macro>

<#macro formPasswordInput path attributes="">
    <@formInput path, attributes, "password"/>
</#macro>

<#macro formHiddenInput path attributes="">
    <@formInput path, attributes, "hidden"/>
</#macro>

<#macro formTextarea path attributes="">
    <@bind path/>
    <textarea id="${status.expression?replace('[','')?replace(']','')}" name="${status.expression}" ${attributes?no_esc}>
${stringStatusValue}</textarea>
</#macro>

<#macro formSingleSelect path options attributes="">
    <@bind path/>
    <select id="${status.expression?replace('[','')?replace(']','')}" name="${status.expression}" ${attributes?no_esc}>
        <#if options?is_hash>
            <#list options?keys as value>
                <option value="${value}"<@checkSelected value/>>${options[value]}</option>
            </#list>
        <#else>
            <#list options as value>
                <option value="${value}"<@checkSelected value/>>${value}</option>
            </#list>
        </#if>
    </select>
</#macro>

<#macro formMultiSelect path options attributes="">
    <@bind path/>
    <select multiple="multiple" id="${status.expression?replace('[','')?replace(']','')}"
            name="${status.expression}" ${attributes?no_esc}>
        <#list options?keys as value>
            <#assign isSelected = contains(status.actualValue![""], value)>
            <option value="${value}"<#if isSelected> selected="selected"</#if>>${options[value]}</option>
        </#list>
    </select>
</#macro>

<#macro formRadioButtons path options separator attributes="">
    <@bind path/>
    <#list options?keys as value>
        <#assign id="${status.expression?replace('[','')?replace(']','')}${value_index}">
        <input type="radio" id="${id}"
               name="${status.expression}" value="${value}"<#if stringStatusValue == value> checked="checked"</#if> ${attributes?no_esc}<@closeTag/>
        <label for="${id}">${options[value]}</label>${separator?no_esc}
    </#list>
</#macro>

<#macro formCheckboxes path options separator attributes="">
    <@bind path/>
    <#list options?keys as value>
        <#assign id="${status.expression?replace('[','')?replace(']','')}${value_index}">
        <#assign isSelected = contains(status.actualValue![""], value)>
        <input type="checkbox" id="${id}"
               name="${status.expression}" value="${value}"<#if isSelected> checked="checked"</#if> ${attributes?no_esc}<@closeTag/>
        <label for="${id}">${options[value]}</label>${separator?no_esc}
    </#list>
    <input type="hidden" name="_${status.expression}" value="on"/>
</#macro>

<#macro formCheckbox path attributes="">
    <@bind path />
    <#assign id="${status.expression?replace('[','')?replace(']','')}">
    <#assign isSelected = status.value?? && status.value?string=="true">
    <input type="hidden" name="_${status.expression}" value="on"/>
    <input type="checkbox" id="${id}"
           name="${status.expression}"<#if isSelected> checked="checked"</#if> ${attributes?no_esc}/>
</#macro>

<#macro showErrors separator classOrStyle="">
    <#list status.errorMessages as error>
        <#if classOrStyle == "">
            <b>${error}</b>
        <#else>
            <#if classOrStyle?index_of(":") == -1><#assign attr="class"><#else><#assign attr="style"></#if>
            <span ${attr}="${classOrStyle}">${error}</span>
        </#if>
        <#if error_has_next>${separator?no_esc}</#if>
    </#list>
</#macro>

<#macro checkSelected value>
    <#if stringStatusValue?is_number && stringStatusValue == value?number>selected="selected"</#if>
    <#if stringStatusValue?is_string && stringStatusValue == value>selected="selected"</#if>
</#macro>

<#function contains list item>
    <#list list as nextInList>
        <#if nextInList == item><#return true></#if>
    </#list>
    <#return false>
</#function>

<#macro closeTag>
    <#if xhtmlCompliant?? && xhtmlCompliant>/><#else>></#if>
</#macro>