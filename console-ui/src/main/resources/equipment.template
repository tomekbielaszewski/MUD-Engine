<#if response.equipment??>
<#assign eq = response.equipment>
Głowa: <#if eq.headItem??>${eq.headItem.name}<#else>pusto</#if>
Tors: <#if eq.torsoItem??>${eq.torsoItem.name}<#else>pusto</#if>
Dłonie: <#if eq.handsItem??>${eq.handsItem.name}<#else>pusto</#if>
Nogi: <#if eq.legsItem??>${eq.legsItem.name}<#else>pusto</#if>
Stopy: <#if eq.feetItem??>${eq.feetItem.name}<#else>pusto</#if>

Broń ręczna: <#if eq.meleeWeapon??>${eq.meleeWeapon.name}<#else>pusto</#if>
Broń miotana: <#if eq.meleeWeapon??>${eq.meleeWeapon.name}<#else>pusto</#if>

<#list backpack?sort_by("name")>
W plecaku masz:
<#items as item>
${item?counter}. ${item.name} x${item.amount}<#sep>,
</#sep>
</#items>

</#list>
</#if>