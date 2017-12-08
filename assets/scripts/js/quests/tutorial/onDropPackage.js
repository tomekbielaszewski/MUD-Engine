//@ sourceURL=assets/scripts/js/quests/tutorial/onDropPackage.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function () {
  function broughtFromBothLocations() {
    return game.player.hasParameter(Quest.ID.packagePickedUpOn.location2) &&
      game.player.hasParameter(Quest.ID.packagePickedUpOn.location4);
  }

  function rewardPlayer() {
    game.player.message("Masz tu kilka miedziaków za uczciwą pracę!");
    Quest.REWARDS.packagesBrought.items.forEach(function (reward) {
      game.player.addItemsByName(reward.item, reward.amount);
    });
    //give player experience -> Quest.REWARDS.packagesBrought.experience
  }

  function startNextQuest() {
    //get documents
    //save parameter about started quest
  }

  function informAboutNextQuest() {
    game.player.message("Weź te papiery i zanieś je sternikowi! Znajdziesz go na dziobie statku. To kolejne zlecenie od jednego z handlarzy w porcie.")
  }

  function onDrop() {
    if (broughtFromBothLocations()) {
      rewardPlayer();
      startNextQuest();
      informAboutNextQuest();
      return;
    }

    game.player.message("Dobry majtek! Jeszcze jeden taki wór i wystarczy.");
  }

  onDrop();
})();