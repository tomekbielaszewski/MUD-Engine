//@ sourceURL=assets/scripts/js/locations/startLocationOnShow.js
//line above is for IntelliJ debugging purposes

var playerName = player.getName();
var welcomeStr = "Witaj " + playerName + "!\n" +
    "Zawitałeś właśnie do krainy MUDa. Pamiętaj, że jak wyjdziesz z tej lokacji, nie możesz już do niej wrócić!";

response.getPlayerEvents()
    .add(welcomeStr);