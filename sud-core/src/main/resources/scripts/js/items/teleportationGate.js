var startingLocationId = "1";
var teleportatioText = "Niesmialo dotykasz czarnej pionowej tafli. Czujesz mocne szarpniecie za reke, obraz ciemnieje... **spojrz** gdzie teraz jestes..."

player.setCurrentLocation(startingLocationId);
response.getPlayerEvents().add(teleportatioText);
