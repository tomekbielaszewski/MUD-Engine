//@ sourceURL=assets/scripts/js/items/teleportationGate.js
//line above is for IntelliJ debugging purposes

var startingLocationId = "1";
var teleportatioText = "Niesmialo dotykasz czarnej pionowej tafli. Czujesz mocne szarpniecie za reke, obraz ciemnieje... Zostałeś wciągnięty wewnatrz bramy...\n"

player.setCurrentLocation(startingLocationId);
response.getPlayerEvents().add(teleportatioText);

commandHandler.execute("spojrz", player, response);

var playerTeleportEvent = "BUM! " + player.getName() + " nagle sie pojawil na lokacji!";
multiplayerNotificationService.broadcast(locationRepo.get(startingLocationId), playerTeleportEvent, player);