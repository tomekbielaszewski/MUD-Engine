//@ sourceURL=assets/scripts/js/quests/tutorial/canPickupPackage.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function hasPackage() {
    return game.player.hasItems(Quest.ITEM.package.id);
  }

  function alreadyPickedUpOnBothLocations() {
    return game.player.hasParameter(Quest.ID.packagePickedUpOn.location2) &&
      game.player.hasParameter(Quest.ID.packagePickedUpOn.location4);
  }

  function alreadyPickedUpOnThisLocation() {
    return game.player.hasParameter(Quest.ID.packagePickedUpOn["location" + game.location.getId()])
  }

  function notOnPackageRespawnLocation() {
    return (Quest.ITEM.package.respawnLocations.indexOf(game.location.getId()) < 0)
  }

  function canPickup() {
    if (notOnPackageRespawnLocation()) {
      game.player.message("Tej roboty nie masz w umowie. Zostaw to!");
      return false;
    }

    if (hasPackage()) {
      game.player.message("Masz już jeden pakunek w rękach!");
      return false;
    }

    if (alreadyPickedUpOnBothLocations()) {
      game.player.message("Juz skonczyles pracowac! Nie musisz już nosić tych pakunków.");
      return false;
    }

    if (alreadyPickedUpOnThisLocation()) {
      game.player.message("Już zaniosłeś stąd pakunek. Zostaw trochę roboty dla innych!");
      return false;
    }

    return true;
  }

  return canPickup();
})();