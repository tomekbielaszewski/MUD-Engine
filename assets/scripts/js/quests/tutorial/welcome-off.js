//@ sourceURL=assets/scripts/js/quests/tutorial/welcome-off.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function (){
  var welcomeTextDeactivated = Quest.ID.welcomeTextDeactivated;

  function deactivateWelcomeText() {
    game.player.addParameter(welcomeTextDeactivated, true);
  }

  function is(parameter) {
    return game.player.hasParameter(parameter);
  }

  if (!is(welcomeTextDeactivated)) {
    deactivateWelcomeText();
  }
})();