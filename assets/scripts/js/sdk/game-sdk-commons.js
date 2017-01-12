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

  return {
    item: item,
    formatText: formatText,
    areItemsIn: areItemsIn,
    sendMessage: sendMessage
  }
})();