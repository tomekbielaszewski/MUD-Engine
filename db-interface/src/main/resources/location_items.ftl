<#if locationItems??>
<#list locationItems?sort_by("name")>
Widzisz tutaj:
<#items as item>
   ${item?counter}. ${item.name} x${item.amount}<#sep>,
</#sep>
</#items>

</#list>
</#if>