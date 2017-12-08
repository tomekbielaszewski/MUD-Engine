//@ sourceURL=assets/scripts/js/sdk/game-sdk-location.js
//line above is for IntelliJ debugging purposes

game.location = (function () {
  var locationID = player.getCurrentLocation();
  var location = locationRepo.get(locationID);

  function hasItems(itemId, amount) {
    var items = location.getItems().getMobileItems();
    return game.areItemsIn(items, itemId, amount);
  }

  function hasItemsByName(itemName, amount) {
    var itemId = itemRepo.getByName(itemName).getId();
    var items = location.getItems().getMobileItems();
    return game.areItemsIn(items, itemId, amount);
  }

  function addItems(itemName, amount) {
    locationService.addItems(itemName, amount || 1, location);
  }

  function message(message, sender, destLocationID) {
    var playerSender = playerRepository.findByNameIgnoreCase(sender || player.getName());
    var destLocation = locationRepo.get(destLocationID || locationID);
    multiplayerNotificationService.broadcast(destLocation, message, playerSender);
  }

  return Object.bindProperties({
    hasItems: hasItems,
    hasItemsByName: hasItemsByName,
    addItems: addItems,
    message: message
  }, location);
})();