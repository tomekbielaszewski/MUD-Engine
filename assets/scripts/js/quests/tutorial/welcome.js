//@ sourceURL=assets/scripts/js/quests/tutorial/welcome.js
//line above is for IntelliJ debugging purposes

(function (){
  var welcomeTextDeactivated = 'tutorial:welcomeTextDeactivated';
  var welcomeTextAlreadyDisplayed = 'tutorial:welcomeTextAlreadyDisplayed';
  var longWelcomeText = 'Pobudka zaspany szczurze lądowy! **Rozejrzyj się**! Dopływamy do portu! Jazda na pokład! Won **na górę** i pomagać przy rozładunku! Nie płyniesz za darmo!';
  var shortWelcomeText = 'Nie słyszałeś!? Dupa w troki i won **na górę**!';

  if(!is(welcomeTextDeactivated)) {
    displayWelcomeText();
  }

  function displayWelcomeText() {
    if (is(welcomeTextAlreadyDisplayed)) {
      tellPlayer(shortWelcomeText);
    } else {
      tellPlayer(longWelcomeText);
      player.addParameter(welcomeTextAlreadyDisplayed, true);
    }
  }

  function is(parameter) {
    return player.hasParameter(parameter);
  }
})();