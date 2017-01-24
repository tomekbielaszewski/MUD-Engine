package org.grizz.game.integration.tests.quests.tutorial;

import org.grizz.game.integration.GameIntegrationTest;
import org.grizz.game.model.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class WelcomeText extends GameIntegrationTest {
    private static final String FIRST_WELCOME_TEXT = "Pobudka zaspany szczurze lądowy!";
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
        player1("spojrz");

        Player playerAfter = fromDB().player(PLAYER1);

        assertThat(playerBefore.hasParameter(FIRST_WELCOME_TEXT_ALREADY_DISPLAYED), is(false));
        assertThat(playerAfter.hasParameter(FIRST_WELCOME_TEXT_ALREADY_DISPLAYED), is(true));
        assertThat(response, hasEventLike(FIRST_WELCOME_TEXT));
    }

    private void secondLookAroundCommandDisplaysShorterWelcomeText() {
        player1("spojrz");

        assertThat(response, hasEvent(AFTER_SHOW_WELCOME_TEXT));
    }

    private void whenMovedAndGoneBackTheWelcomeTextIsGone() {
        Player playerBefore = fromDB().player(PLAYER1);
        player1("west");
        player1("east");

        Player playerAfter = fromDB().player(PLAYER1);

        assertThat(playerBefore.hasParameter(WELCOME_TEXT_DEACTIVATED), is(false));
        assertThat(playerAfter.hasParameter(WELCOME_TEXT_DEACTIVATED), is(true));
        assertThat(response.getPlayerEvents(), hasSize(0));
    }
}
