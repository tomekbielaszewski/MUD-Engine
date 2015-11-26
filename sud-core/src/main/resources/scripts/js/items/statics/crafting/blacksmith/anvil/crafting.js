//@ sourceURL=sud-core/src/main/resources/scripts/js/items/statics/crafting/blacksmith/anvil/crafting.js
//line above is for IntelliJ debugging purposes
loadScript("bs-anvil-materials");
loadScript("bs-anvil-recipes");
loadScript("crafting");

var blacksmithsHammer = "9";

function doesPlayerHaveRequiredTools() {
    return playerHas(blacksmithsHammer);
}

function informThatPlayerHasNoRequiredTools() {
    tellPlayer("Aby korzystać z kowadła musisz mieć młot kowalski!");
}

interpretCommand(function (commandSplit) {
    var itemName = commandSplit[0];
    var amount = commandSplit.length == 2 ? commandSplit[1] : 1;

    logger.info("{} is crafting {}x {} on anvil", [player.getName(), amount, itemName]);
    craft(itemName, amount);
});