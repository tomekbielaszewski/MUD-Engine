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

var amountToCraft = getOrDefault('amount', 1);

logger.info("{} is crafting {}x {} on anvil", [player.getName(), amountToCraft, itemName]);
craft(itemName, amountToCraft);