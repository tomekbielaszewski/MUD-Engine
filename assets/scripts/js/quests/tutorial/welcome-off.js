//@ sourceURL=assets/scripts/js/quests/tutorial/welcome-off.js
//line above is for IntelliJ debugging purposes

(function (){
  var welcomeTextDeactivated = 'tutorial:welcomeTextDeactivated';

  if(!is(welcomeTextDeactivated)) {
    deactivateWelcomeText();
  }

  function deactivateWelcomeText() {
    player.addParameter(welcomeTextDeactivated, true);
  }

  function is(parameter) {
    return player.hasParameter(parameter);
  }
})();