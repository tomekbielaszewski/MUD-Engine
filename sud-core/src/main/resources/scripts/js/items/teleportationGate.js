var startingLocationId = "1";
var teleportatioText = "Niesmialo dotykasz czarnej pionowej tafli. Czujesz mocne szarpniecie za reke, obraz ciemnieje... Zostałeś wciągnięty wewnatrz bramy...\n"

player.setCurrentLocation(startingLocationId);
response.getPlayerEvents().add(teleportatioText);

commandRunner.execute("spojrz", player, response);

var playerTeleportEvent = eventService.getEvent("multiplayer.event.player.teleported.to.location", [player.getName()]);
var targetLocation = locationRepo.get(startingLocationId);
notificationService.broadcast(targetLocation, playerTeleportEvent, player);