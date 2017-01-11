//@ sourceURL=assets/scripts/js/quests/tutorial/respawnPackage.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function locationHasNoPackage() {
    return !game.location.hasItems(Quest.ITEM.package.id);
  }

  function respawnPackage() {
    game.location.addItems(Quest.ITEM.package.name);
  }

  if (locationHasNoPackage()) {
    respawnPackage();
  }
})();