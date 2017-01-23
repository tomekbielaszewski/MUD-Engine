//@ sourceURL=assets/scripts/js/quests/tutorial/canDropPackage.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function isOnPackageCollectingPoint() {
    var onPackageCollectingPoint = game.location.getId().equals(Quest.ITEM.package.collectingPoint);

    if (!onPackageCollectingPoint) {
      game.player.message("Gdzie to kładziesz?! Punkt rozładunku jest na pokładzie!");
    }
    return onPackageCollectingPoint;
  }

  return isOnPackageCollectingPoint();
})();