<#list response.players>
<#include "/separator.ftl">
Obok ciebie stoją <#items as player>${player}<#sep>, </#sep></#items>
</#list>