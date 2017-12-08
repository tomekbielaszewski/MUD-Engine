//@ sourceURL=assets/scripts/js/items/statics/crafting/blacksmith/furnace/crafting.js
//line above is for IntelliJ debugging purposes
loadScript("bs-furnace-materials");
loadScript("bs-furnace-recipes");
loadScript("crafting");

var doesPlayerHaveRequiredTools;
var informThatPlayerHasNoRequiredTools;

(function () {
  var tongs = "8";
  var amountToCraft = getOrDefault('amount', 1);

  doesPlayerHaveRequiredTools = function () {
    return game.player.hasItems(tongs);
  };

  informThatPlayerHasNoRequiredTools = function () {
    game.player.message("Aby korzystać z pieca musisz mieć szczypce kowalskie!");
  };

  logger.info("{} is crafting {}x {} on anvil", [game.player.getName(), amountToCraft, itemName]);
  game.crafting.make(itemName, amountToCraft);
})();