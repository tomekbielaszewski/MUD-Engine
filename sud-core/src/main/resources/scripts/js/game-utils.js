//@ sourceURL=sud-core/src/main/resources/scripts/js/game-utils.js
//line above is for IntelliJ debugging purposes

function interpretCommand(executor) {
    var commandSplit = commandUtils.splitCommand(command, commandPattern);
    executor(commandSplit);
}

function tellPlayer(message) {
    response.getPlayerEvents().add(message);
}

function playerHas(itemId, amount) {
    var items = equipmentService.getItemsInEquipment(player);
    var itemsMatching = items.stream()
        .filter(function (item) {
            return item.getId() === itemId;
        }).collect(java.util.stream.Collectors.toList());

    return itemsMatching.size() >= amount;
}