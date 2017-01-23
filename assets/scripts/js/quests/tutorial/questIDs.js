//@ sourceURL=assets/scripts/js/quests/tutorial/questIDs.js
//line above is for IntelliJ debugging purposes

var Quest = (function () {
  return {
    ID: {
      welcomeTextDeactivated: "quest:tutorial-welcome-text-deactivated",
      welcomeTextAlreadyDisplayed: "quest:tutorial-text-already-displayed",
      packagePickedUpOn: {
        location2: "quest:tutorial-ship-package-picked-on-locations-2",
        location4: "quest:tutorial-ship-package-picked-on-locations-4"
      }
    },
    ITEM: {
      package: {
        name: "Wór z towarem",
        id: "1000",
        respawnLocations: ["2", "4"]
      }
    }
  }
})();