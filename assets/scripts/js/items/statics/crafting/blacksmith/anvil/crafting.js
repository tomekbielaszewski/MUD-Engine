//@ sourceURL=assets/scripts/js/items/statics/crafting/blacksmith/anvil/crafting.js
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

var amountToCraft = getOrDefault('amount', 1);

logger.info("{} is crafting {}x {} on anvil", [player.getName(), amountToCraft, itemName]);
craft(itemName, amountToCraft);