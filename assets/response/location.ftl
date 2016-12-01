<#if response.currentLocation??>
**${response.currentLocation.name}**

${response.currentLocation.description}
<#include "/static_items.ftl">
<#include "/location_items.ftl">
<#include "/players_on_location.ftl">
<#include "/location_exits.ftl">
<#include "/separator.ftl">
</#if>
