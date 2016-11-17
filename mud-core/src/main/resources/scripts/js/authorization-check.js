//@ sourceURL=mud-core/src/main/resources/scripts/js/authorization-check.js
//line above is for IntelliJ debugging purposes

var admins = ["Grizwold", "GameMaster"];

function isAuthorized(name) {
    return admins.indexOf(name) > -1;
}
isAuthorized(player.getName()); //last return from the script is the script return value