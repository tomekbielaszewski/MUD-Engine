<#list response.players>
<#include "/separator.ftl">
Obok ciebie <#if response.players?size gt 1>stoją<#else>stoi</#if> <#items as player>${player}<#sep>, </#sep></#items>
</#list>