//@ sourceURL=assets/scripts/js/sdk/game-sdk.js
//line above is for IntelliJ debugging purposes


function getOrDefault(variableName, defaultValue) {
  return this[variableName] !== undefined ? this[variableName] : defaultValue;
}

var game = (function () {

  function formatText() {
    var string = arguments[0];
    var args = [];
    for (var i = 1; i < arguments.length; i++) {
      args.push(arguments[i]);
    }

    return java.lang.String.format(string, args);
  }

  function areItemsIn(itemList, itemId, amount) {
    amount = amount || 1;
    var itemsMatching = itemList.stream()
      .filter(function (item) {
        return item.getId() === itemId;
      }).count();

    return itemsMatching >= amount;
  }

  function sendMessage(playerName, message) {
    var receiver = playerRepository.findByNameIgnoreCase(playerName);
    multiplayerNotificationService.send(receiver, message);
  }

  function item(id) {
    return itemRepo.get(id);
  }

  function currentPlayer() {
    function hasItems(itemId, amount) {
      var items = player.getEquipment().getBackpack();
      return areItemsIn(items, itemId, amount);
    }

    function hasItemsByName(itemName, amount) {
      var itemId = itemRepo.getByName(itemName).getId();
      var items = player.getEquipment().getBackpack();
      return areItemsIn(items, itemId, amount);
    }

    function addItemsByName(itemName, amount) {
      equipmentService.addItems(itemName, amount, player, response);
    }

    function addItems(itemId, amount) {
      var itemName = itemRepo.get(itemId).getName();
      addItemsByName(itemName, amount);
    }

    function removeItemsByName(itemName, amount) {
      return equipmentService.removeItems(itemName, amount, player, response);
    }

    function removeItems(itemId, amount) {
      var itemName = itemRepo.get(itemId).getName();
      return removeItemsByName(itemName, amount)
    }

    function message(message) {
      response.getPlayerEvents().add(message);
    }

    return Object.bindProperties({
      addItems: addItems,
      addItemsByName: addItemsByName,
      removeItems: removeItems,
      removeItemsByName: removeItemsByName,
      hasItems: hasItems,
      hasItemsByName: hasItemsByName,
      message: message
    }, player);
  }

  function currentLocation() {
    var locationID = player.getCurrentLocation();
    var location = locationRepo.get(locationID);

    function hasItems(itemId, amount) {
      var items = location.getItems().getMobileItems();
      return areItemsIn(items, itemId, amount);
    }

    function hasItemsByName(itemName, amount) {
      var itemId = itemRepo.getByName(itemName).getId();
      var items = location.getItems().getMobileItems();
      return areItemsIn(items, itemId, amount);
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
  }

  return {
    item: item,
    formatText: formatText,
    areItemsIn: areItemsIn,
    sendMessage: sendMessage,
    player: currentPlayer(),
    location: currentLocation()
  }
})();