//@ sourceURL=assets/scripts/js/items/statics/crafting/blacksmith/anvil/crafting.js
//line above is for IntelliJ debugging purposes
loadScript("bs-anvil-materials");
loadScript("bs-anvil-recipes");
loadScript("crafting");

var doesPlayerHaveRequiredTools;
var informThatPlayerHasNoRequiredTools;

(function () {
  var blacksmithsHammer = "9";
  var amountToCraft = getOrDefault('amount', 1);

  doesPlayerHaveRequiredTools = function () {
    return game.player.hasItems(blacksmithsHammer);
  };

  informThatPlayerHasNoRequiredTools = function () {
    game.player.message("Aby korzystać z kowadła musisz mieć młot kowalski!");
  };

  logger.info("{} is crafting {}x {} on anvil", [game.player.getName(), amountToCraft, itemName]);
  game.crafting.make(itemName, amountToCraft);
})();