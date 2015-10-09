var startingLocationId = "1";
var teleportatioText = "Niesmialo dotykasz czarnej pionowej tafli. Czujesz mocne szarpniecie za reke, obraz ciemnieje... Zostałeś wciągnięty wewnatrz bramy..."

player.setCurrentLocation(startingLocationId);
response.getPlayerEvents().add(teleportatioText);

commandRunner.execute("spojrz", player, response);