//@ sourceURL=sud-core/src/main/resources/scripts/js/items/statics/crafting/blacksmith/furnace/crafting.js
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

interpretCommand(function (commandSplit) {
    var itemName = commandSplit[0];
    var amount = commandSplit.length == 2 ? commandSplit[1] : 1;

    logger.info("{} is crafting {}x {} on furnace", [player.getName(), amount, itemName]);
    craft(itemName, amount);
});