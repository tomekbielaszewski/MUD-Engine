//@ sourceURL=mud-core/src/main/resources/scripts/js/items/statics/crafting/blacksmith/anvil/crafting.js
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

var itemName = commandUtils.getVariable("itemName", command, commandPattern);
var amount = commandUtils.getVariableOrDefaultValue("amount", "1", command, commandPattern);

logger.info("{} is crafting {}x {} on anvil", [player.getName(), amount, itemName]);
craft(itemName, amount);