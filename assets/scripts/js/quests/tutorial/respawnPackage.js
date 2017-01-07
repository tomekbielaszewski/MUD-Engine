//@ sourceURL=assets/scripts/js/quests/tutorial/respawnPackage.js
//line above is for IntelliJ debugging purposes

(function () {
  var packageId = "";

  function locationHasNoPackage() {
    return !locationHasItems(packageId);
  }

  function respawnPackage() {

  }

  if (locationHasNoPackage()) {
    respawnPackage();
  }
})();