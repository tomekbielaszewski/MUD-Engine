//@ sourceURL=assets/scripts/js/quests/tutorial/respawnPackage.js
//line above is for IntelliJ debugging purposes

(function () {
  var packageName = "Wór z towarem";

  function locationHasNoPackage() {
    return !location.hasItemsByName(packageName);
  }

  function respawnPackage() {
    locationService.addItems(packageName, 1, location);
  }

  if (locationHasNoPackage()) {
    respawnPackage();
  }
})();