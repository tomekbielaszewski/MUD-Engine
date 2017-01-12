//@ sourceURL=assets/scripts/js/sdk/game-sdk-player.js
//line above is for IntelliJ debugging purposes

game.player = (function () {
  function hasItems(itemId, amount) {
    var items = player.getEquipment().getBackpack();
    return game.areItemsIn(items, itemId, amount);
  }

  function hasItemsByName(itemName, amount) {
    var itemId = itemRepo.getByName(itemName).getId();
    var items = player.getEquipment().getBackpack();
    return game.areItemsIn(items, itemId, amount);
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
})();