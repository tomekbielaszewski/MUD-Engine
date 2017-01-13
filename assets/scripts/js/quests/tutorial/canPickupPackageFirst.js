//@ sourceURL=assets/scripts/js/quests/tutorial/canPickupPackageFirst.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function hasPackage() {
    return game.player.hasItems(Quest.ITEM.package.id);
  }

  function alreadyBroughtThisPackage() {
    return game.player.hasParameter(Quest.ID.firstPackageBrought);
  }

  function alreadyBroughtBothPackages() {
    return game.player.hasParameter(Quest.ID.firstPackageBrought) &&
      game.player.hasParameter(Quest.ID.secondPackageBrought);
  }

  function canPickup() {
    if (hasPackage()) {
      game.player.message("Masz już jeden pakunek w rękach!");
      return false;
    }

    if (alreadyBroughtBothPackages()) {
      game.player.message("Robota wykonana! Nie musisz już nosić tych pakunków.");
      return false;
    }

    if (alreadyBroughtThisPackage()) {
      game.player.message("Już to zaniosłeś! Poszukaj w innej części ładowni.");
      return false;
    }

    return true;
  }

  return canPickup();
})();