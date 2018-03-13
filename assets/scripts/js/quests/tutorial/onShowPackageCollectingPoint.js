//@ sourceURL=assets/scripts/js/quests/tutorial/onShowPackageCollectingPoint.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function broughtPackageFromBothLocations() {
    return game.player.hasParameter(Quest.ID.packagePickedUpOn.location2) &&
      game.player.hasParameter(Quest.ID.packagePickedUpOn.location4);
  }

  function hasPackage() {
    return game.player.hasItems(Quest.ITEM.package.id);
  }

  function captainDocumentsQuestStarted() {
    return game.player.hasParameter(Quest.ID.captainDocuments.started);
  }

  function captainDocumentsQuestFinished() {
    return game.player.hasParameter(Quest.ID.captainDocuments.finished);
  }

  function onShowPackageCollectingPoint() {
    if (hasPackage() && !broughtPackageFromBothLocations()) {
      game.player.message("Dawaj to tu! **Połóż wór z towarem** tutaj!")
    }

    if (broughtPackageFromBothLocations() && !captainDocumentsQuestStarted()) {
      game.player.message("Dobry z ciebie majtek! Poloz to i robota skończona!")
    }

    if (captainDocumentsQuestStarted()) {
      if (!captainDocumentsQuestFinished()) {
        game.player.message("Zanieś wreszcie te dokumenty sternikowi! Zaraz odpływamy!");
      }
    }
  }

  onShowPackageCollectingPoint();
})();