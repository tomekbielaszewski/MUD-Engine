//@ sourceURL=assets/scripts/js/authorization-check.js
//line above is for IntelliJ debugging purposes

(function () {
  var admins = ["Grizwold", "GameMaster"];

  function isAuthorized(name) {
    return admins.indexOf(name) > -1;
  }

  return isAuthorized(game.player.getName());
})();