//@ sourceURL=assets/scripts/js/quests/tutorial/needToTakePackageBeforeEnterUpperDeck.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function tookPackageFromAnyLocations() {
    return game.player.hasParameter(Quest.ID.packagePickedUpOn.location2) ||
      game.player.hasParameter(Quest.ID.packagePickedUpOn.location4);
  }

  function needToTakePackageBeforeEnterUpperDeck() {
    var packageTakenBefore = tookPackageFromAnyLocations();

    if (!packageTakenBefore) {
      game.player.message("Gdzie leziesz z pustymi łapami?! Mówiłem pomagać przy rozładunku! Szukaj worów z ziarnem! " +
        "Są gdzieś na **wschodzie** czy **zachodzie** ładowni. **Weź wór z towarem** i zanieś go na południową część pokładu! Tak! Tą od strony portu. DO ROBOTY!");

      loadScript("tutorial-welcome-off");
    }

    return packageTakenBefore;
  }

  return needToTakePackageBeforeEnterUpperDeck();
})();