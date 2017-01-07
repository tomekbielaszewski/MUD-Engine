//@ sourceURL=assets/scripts/js/sdk/game-sdk.js
//line above is for IntelliJ debugging purposes

var game = (function () {
  function currentLocationId() {
    return player.getCurrentLocation();
  }

  function currentLocation() {
    return locationRepo.get(currentLocationId());
  }

  function areItemsIn(itemList, itemId, amount) {
    if (amount === undefined) {
      amount = 1;
    }
    var itemsMatching = itemList.stream()
      .filter(function (item) {
        return item.getId() === itemId;
      }).count();

    return itemsMatching >= amount;
  }

  return {
    currentLocation: currentLocation,
    currentLocationId: currentLocationId,
    areItemsIn: areItemsIn
  }
})();

// (function () {
//   location.hasItems = function(itemId, amount) {
//     var items = this.getItems().getMobileItems();
//     return game.areItemsIn(items, itemId, amount);
//   };
//
//   location.hasItemsByName = function(itemName, amount) {
//     var itemId = itemRepo.getByName(itemName).getId();
//     var items = this.getItems().getMobileItems();
//     return game.areItemsIn(items, itemId, amount);
//   };
// })();

(function () {
  player.hasItems = function (itemId, amount) {
    var items = this.getEquipment().getBackpack();
    return game.areItemsIn(items, itemId, amount);
  };

  player.hasItemsByName = function (itemName, amount) {
    var itemId = itemRepo.getByName(itemName).getId();
    var items = this.getEquipment().getBackpack();
    return game.areItemsIn(items, itemId, amount);
  };
})();