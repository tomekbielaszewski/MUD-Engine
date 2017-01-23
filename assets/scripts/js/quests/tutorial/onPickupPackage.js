//@ sourceURL=assets/scripts/js/quests/tutorial/onPickupPackage.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function savePackagePickUp() {
    game.player.addParameter(Quest.ID.packagePickedUpOn["location" + game.location.getId()], true);
  }

  function onPickup() {
    savePackagePickUp();
  }

  onPickup();
})();