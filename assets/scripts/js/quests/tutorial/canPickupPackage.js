//@ sourceURL=assets/scripts/js/quests/tutorial/canPickupPackage.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function hasPackage() {
    return game.player.hasItems(Quest.ITEM.package.id);
  }

  function alreadyBroughtPackage() {
    return game.player.hasParameter(Quest.ID.firstPackageBrought);
  }

  function canPickup() {
    var canPickup = true;

    if (hasPackage()) {
      game.player.message("Masz już jeden pakunek w rękach!");
      canPickup = false;
    }

    if (alreadyBroughtPackage()) {
      game.player.message("Już to zaniosłeś! Poszukaj w innej części ładowni.");
      canPickup = false;
    }
    return canPickup;
  }

  return canPickup();
})();