//@ sourceURL=assets/scripts/js/quests/tutorial/respawnPackage.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function locationHasPackage() {
    return game.location.hasItems(Quest.ITEM.package.id);
  }

  function playerHasAlreadyTakenPackageFromThisLocation() {
    return game.player.hasParameter(Quest.ID.packagePickedUpOn["location" + game.location.getId()]);
  }

  function respawnPackage() {
    game.location.addItems(Quest.ITEM.package.name);
  }

  if (!playerHasAlreadyTakenPackageFromThisLocation()) {
    game.player.message("Zanieś ten towar na pokład!");
  }

    if (!locationHasPackage() && !playerHasAlreadyTakenPackageFromThisLocation()) {
    respawnPackage();
  }
})();