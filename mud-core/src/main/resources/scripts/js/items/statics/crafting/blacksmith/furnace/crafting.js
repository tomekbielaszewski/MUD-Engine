//@ sourceURL=mud-core/src/main/resources/scripts/js/items/statics/crafting/blacksmith/furnace/crafting.js
//line above is for IntelliJ debugging purposes
loadScript("bs-furnace-materials");
loadScript("bs-furnace-recipes");
loadScript("crafting");

var tongs = "8";

function doesPlayerHaveRequiredTools() {
    return playerHas(tongs);
}

function informThatPlayerHasNoRequiredTools() {
    tellPlayer("Aby korzystać z pieca musisz mieć szczypce kowalskie!");
}

var itemName = commandUtils.getVariable("itemName", command, commandPattern);
var amount = commandUtils.getVariableOrDefaultValue("amount", "1", command, commandPattern);

logger.info("{} is crafting {}x {} on anvil", [player.getName(), amount, itemName]);
craft(itemName, amount);