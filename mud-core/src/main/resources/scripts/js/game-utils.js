//@ sourceURL=mud-core/src/main/resources/scripts/js/game-utils.js
//line above is for IntelliJ debugging purposes

function format() {
    var string = arguments[0];
    var args = [];
    for (i = 1; i < arguments.length; i++) {
        args.push(arguments[i]);
    }

    return java.lang.String.format(string, args);
}

function tellPlayer(message) {
    response.getPlayerEvents().add(message);
}

function sendMessage(message, playerName) {
    var player = playerRepo.findByNameIgnoreCase(playerName);
    notificationService.send(player, message);
}

function sendMessageToAllOnLocation(message, sender, locationId) {
    var playerSender = playerRepo.findByNameIgnoreCase(sender);
    var location = locationRepo.get(locationId);
    notificationService.broadcast(location, message, playerSender);
}

function playerHas(itemId, amount) {
    if (amount === undefined) {
        amount = 1;
    }
    var items = equipmentService.getItemsInEquipment(player);
    var itemsMatching = items.stream()
        .filter(function (item) {
            return item.getId() === itemId;
        }).collect(java.util.stream.Collectors.toList());

    return itemsMatching.size() >= amount;
}