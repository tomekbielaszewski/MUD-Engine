<#if response.playerEvents?size gt 0>
<#list response.playerEvents as event>
${event}
</#list>
<#include "/separator.ftl">
</#if>
