//@ sourceURL=assets/scripts/js/quests/tutorial/respawnPackage.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function locationHasNoPackage() {
    return !game.location.hasItems(Quest.ITEM.package.id);
  }

  function playerHasNotTakenPackageFromThisLocation() {
    var hasNotTakenPackageBefore = !game.player.hasParameter(Quest.ID.packagePickedUpOn["location" + game.location.getId()]);

    if (hasNotTakenPackageBefore) {
      game.player.message("Zanieś ten towar na pokład!");
    }

    return hasNotTakenPackageBefore;
  }

  function respawnPackage() {
    game.location.addItems(Quest.ITEM.package.name);
  }

  if (locationHasNoPackage() && playerHasNotTakenPackageFromThisLocation()) {
    respawnPackage();
  }
})();