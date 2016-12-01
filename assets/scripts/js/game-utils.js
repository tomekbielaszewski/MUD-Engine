//@ sourceURL=assets/scripts/js/game-utils.js
//line above is for IntelliJ debugging purposes

function format() {
    var string = arguments[0];
    var args = [];
    for (i = 1; i < arguments.length; i++) {
        args.push(arguments[i]);
    }

    return java.lang.String.format(string, args);
}

function getOrDefault(variableName, defaultValue) {
    return this[variableName] !== undefined ? this[variableName] : defaultValue;
}

function tellPlayer(message) {
    response.getPlayerEvents().add(message);
}

function sendMessage(message, playerName) {
    var player = playerRepository.findByNameIgnoreCase(playerName);
    multiplayerNotificationService.send(player, message);
}

function sendMessageToAllOnLocation(message, sender, locationId) {
    var playerSender = playerRepository.findByNameIgnoreCase(sender);
    var location = locationRepo.get(locationId);
    multiplayerNotificationService.broadcast(location, message, playerSender);
}

function playerHas(itemId, amount) {
    if (amount === undefined) {
        amount = 1;
    }
    var items = player.getEquipment().getBackpack();
    var itemsMatching = items.stream()
        .filter(function (item) {
            return item.getId() === itemId;
        }).collect(java.util.stream.Collectors.toList());

    return itemsMatching.size() >= amount;
}