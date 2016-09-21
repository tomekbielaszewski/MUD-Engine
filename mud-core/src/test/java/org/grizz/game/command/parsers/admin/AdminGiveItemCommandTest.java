package org.grizz.game.command.parsers.admin;

import org.grizz.game.command.executors.admin.AdminGiveItemCommandExecutor;
import org.grizz.game.command.executors.admin.AdminTeleportCommandExecutor;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminGiveItemCommandTest {
    private static final String DEFAULT_PLAYER_NAME = "default player name";

    @Mock
    private Environment environment;
    @Mock
    private AdminGiveItemCommandExecutor adminCommandExecutor;

    @InjectMocks
    private AdminGiveItemCommand command = new AdminGiveItemCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("daj (?<amount>[\\d]+) (?<itemName>[\\D]+) graczowi (?<playerName>[\\w-]{4,});" +
                        "daj (?<itemName>[\\D]+) graczowi (?<playerName>[\\w-]{4,})");
    }

    @Test
    public void testAccept() throws Exception {
        assertTrue(command.accept("daj mlot bojowy graczowi Grizz"));
        assertTrue(command.accept("daj 2 mlot bojowy graczowi Grizz"));
        assertTrue(command.accept("daj mlot bojowy graczowi Grizz-lol"));
        assertTrue(command.accept("daj 2 mlot bojowy graczowi Grizz-lol"));
        assertTrue(command.accept("daj 22 mlot bojowy graczowi Grizz-lol"));
        assertTrue(command.accept("daj mlot bojowy graczowi Grizz_lol"));
        assertTrue(command.accept("daj 2 mlot bojowy graczowi Grizz_lol"));
        assertTrue(command.accept("daj 22 mlot bojowy graczowi Grizz_lol"));

        assertFalse(command.accept("daj mlot bojowy graczowi \n"));
        assertFalse(command.accept("daj 2 mlot bojowy graczowi \n"));
        assertFalse(command.accept("daj  graczowi Grizz"));
        assertFalse(command.accept("daj 2  graczowi Grizz"));
        assertFalse(command.accept("daj graczowi Grizz\n"));
        assertFalse(command.accept("daj 2 graczowi Grizz\n"));
        assertFalse(command.accept("daj mlot bojowy graczowi "));
        assertFalse(command.accept("daj 2 mlot bojowy graczowi "));
        assertFalse(command.accept("daj 22 2 mlot bojowy graczowi Grizz lol\n"));
    }

    @Test
    public void testExecute() throws Exception {
        verifyPassedVariables("daj mlot bojowy graczowi Grizz", "Grizz", "mlot bojowy", 1);
        verifyPassedVariables("daj 2 mlot bojowy graczowi Grizz", "Grizz", "mlot bojowy", 2);
        verifyPassedVariables("daj 22 mlot bojowy graczowi Grizz", "Grizz", "mlot bojowy", 22);

        verifyPassedVariables("daj mlot bojowy graczowi Grizz-lol", "Grizz-lol", "mlot bojowy", 1);
        verifyPassedVariables("daj mlot bojowy graczowi Grizz_lol", "Grizz_lol", "mlot bojowy", 1);
        verifyPassedVariables("daj mlot bojowy graczowi 22Grizz_lol", "22Grizz_lol", "mlot bojowy", 1);

        verifyPassedVariables("daj topor graczowi Grizz", "Grizz", "topor", 1);
        verifyPassedVariables("daj mloto-nadziak graczowi Grizz", "Grizz", "mloto-nadziak", 1);
    }

    private void verifyPassedVariables(String userCommand, String expectedPlayerName, String expectedItemName, int expectedAmount) {
        Player player = Player.builder().name(DEFAULT_PLAYER_NAME).build();
        PlayerResponse response = new PlayerResponse();

        command.execute(userCommand, player, response);
        verify(adminCommandExecutor).give(expectedPlayerName, expectedItemName, expectedAmount, player, response);
        verifyNoMoreInteractions(adminCommandExecutor);
    }
}