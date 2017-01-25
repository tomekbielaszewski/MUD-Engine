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
      },
      captainDocuments: {
        started: "quest:tutorial-ship-captain-documents-started",
        finished: "quest:tutorial-ship-captain-documents-finished"
      }
    },
    ITEM: {
      package: {
        name: "Wór z towarem",
        id: "1000",
        respawnLocations: ["2", "4"],
        collectingPoint: "6"
      }
    },
    REWARDS: {
      packagesBrought: {
        items: [{
          item: "Brązowa moneta",
          amount: 4
        }],
        experience: 100
      }
    }
  }
})();