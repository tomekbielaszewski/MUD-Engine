package org.grizz.game.integration.tests.quests.tutorial;

import org.grizz.game.integration.IntegrationTest;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class WelcomeText extends IntegrationTest {
    private static final String FIRST_WELCOME_TEXT = "Pobudka zaspany szczurze lądowy! **Rozejrzyj się**! Dopływamy do portu! Jazda na pokład! Won **na górę** i pomagać przy rozładunku! Nie płyniesz za darmo!";
    private static final String AFTER_SHOW_WELCOME_TEXT = "Nie słyszałeś!? Dupa w troki i won **na górę**!";

    private static final String FIRST_WELCOME_TEXT_ALREADY_DISPLAYED = "quest:tutorial-text-already-displayed";
    private static final String WELCOME_TEXT_DEACTIVATED = "quest:tutorial-welcome-text-deactivated";

    @Test
    public void testTutorialWelcomeText() {
        firstLookAroundCommandDisplaysLongerWelcomeText();
        secondLookAroundCommandDisplaysShorterWelcomeText();
        whenMovedAndGoneBackTheWelcomeTextIsGone();
    }

    private void firstLookAroundCommandDisplaysLongerWelcomeText() {
        Player playerBefore = fromDB().player(PLAYER1);
        assertFalse(playerBefore.hasParameter(FIRST_WELCOME_TEXT_ALREADY_DISPLAYED));

        PlayerResponse response = game.runCommand("spojrz", PLAYER1);

        assertThat(response.getPlayerEvents(), hasItem(FIRST_WELCOME_TEXT));
        Player playerAfter = fromDB().player(PLAYER1);
        assertTrue(playerAfter.hasParameter(FIRST_WELCOME_TEXT_ALREADY_DISPLAYED));
    }

    private void secondLookAroundCommandDisplaysShorterWelcomeText() {
        PlayerResponse response = game.runCommand("spojrz", PLAYER1);

        assertThat(response.getPlayerEvents(), hasItem(AFTER_SHOW_WELCOME_TEXT));
    }

    private void whenMovedAndGoneBackTheWelcomeTextIsGone() {
        Player playerBefore = fromDB().player(PLAYER1);
        assertFalse(playerBefore.hasParameter(WELCOME_TEXT_DEACTIVATED));

        game.runCommand("west", PLAYER1);
        PlayerResponse response = game.runCommand("east", PLAYER1);

        Player playerAfter = fromDB().player(PLAYER1);
        assertTrue(playerAfter.hasParameter(WELCOME_TEXT_DEACTIVATED));
        assertThat(response.getPlayerEvents(), IsCollectionWithSize.hasSize(0));
    }
}
