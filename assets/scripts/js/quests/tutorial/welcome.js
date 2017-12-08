//@ sourceURL=assets/scripts/js/quests/tutorial/welcome.js
//line above is for IntelliJ debugging purposes

loadScript("tutorial-quest-ids");

(function (){
  var welcomeTextDeactivated = Quest.ID.welcomeTextDeactivated;
  var welcomeTextAlreadyDisplayed = Quest.ID.welcomeTextAlreadyDisplayed;

  var longWelcomeText = 'Pobudka zaspany szczurze lądowy! **Rozejrzyj się**! Dopływamy do portu! Jazda na pokład! Won **na górę** i pomagać przy rozładunku! Nie płyniesz za darmo!';
  var shortWelcomeText = 'Nie słyszałeś!? Dupa w troki i won **na górę**!';

  function displayWelcomeText() {
    if (is(welcomeTextAlreadyDisplayed)) {
      game.player.message(shortWelcomeText);
    } else {
      game.player.message(longWelcomeText);
      game.player.addParameter(welcomeTextAlreadyDisplayed, true);
    }
  }

  function is(parameter) {
    return game.player.hasParameter(parameter);
  }

  if (!is(welcomeTextDeactivated)) {
    displayWelcomeText();
  }
})();