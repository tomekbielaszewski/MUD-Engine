var startingLocationId = "1";
var teleportatioText = "Niesmialo dotykasz czarnej pionowej tafli. Czujesz mocne szarpniecie za reke, obraz ciemnieje... Zostałeś wciągnięty wewnatrz bramy...\n"

player.setCurrentLocation(startingLocationId);
response.getPlayerEvents().add(teleportatioText);

commandRunner.execute("spojrz", player, response);

var playerTeleportEvent = "BUM! " + player.getName() + " nagle sie pojawil na lokacji!";
notificationService.broadcast(locationRepo.get(startingLocationId), playerTeleportEvent, player);