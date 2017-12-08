package org.grizz.game.command.parsers.system;

import org.grizz.game.command.executors.system.DropCommandExecutor;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DropCommandTest {

    @Mock
    private DropCommandExecutor commandExecutor;
    @Mock
    private Environment environment;

    @InjectMocks
    private DropCommand command = new DropCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("wyrzuc (?<itemName>[\\D]+) (?<amount>[\\d]+);" +
                        "porzuc (?<itemName>[\\D]+) (?<amount>[\\d]+);" +
                        "upusc (?<itemName>[\\D]+) (?<amount>[\\d]+);" +
                        "poloz (?<itemName>[\\D]+) (?<amount>[\\d]+);" +
                        "wyrzuc (?<amount>[\\d]+) (?<itemName>[\\D]+);" +
                        "porzuc (?<amount>[\\d]+) (?<itemName>[\\D]+);" +
                        "upusc (?<amount>[\\d]+) (?<itemName>[\\D]+);" +
                        "poloz (?<amount>[\\d]+) (?<itemName>[\\D]+);" +
                        "wyrzuc (?<itemName>[\\D]+);" +
                        "porzuc (?<itemName>[\\D]+);" +
                        "upusc (?<itemName>[\\D]+);" +
                        "poloz (?<itemName>[\\D]+)");
    }

    @Test
    public void accept() throws Exception {
        assertTrue(command.accept("wyrzuc mlot bojowy 22"));
        assertTrue(command.accept("porzuc mlot bojowy 22"));
        assertTrue(command.accept("upusc mlot bojowy 22"));
        assertTrue(command.accept("poloz mlot bojowy 22"));
        assertTrue(command.accept("wyrzuc 22 mlot bojowy"));
        assertTrue(command.accept("porzuc 22 mlot bojowy"));
        assertTrue(command.accept("upusc 22 mlot bojowy"));
        assertTrue(command.accept("poloz 22 mlot bojowy"));
        assertTrue(command.accept("wyrzuc mlot bojowy"));
        assertTrue(command.accept("porzuc mlot bojowy"));
        assertTrue(command.accept("upusc mlot bojowy"));
        assertTrue(command.accept("poloz mlot bojowy"));

        assertFalse(command.accept("wyrzuc "));
        assertFalse(command.accept("porzuc "));
        assertFalse(command.accept("upusc "));
        assertFalse(command.accept("poloz "));
        assertFalse(command.accept("wyrzuc 22"));
        assertFalse(command.accept("porzuc 22"));
        assertFalse(command.accept("upusc 22"));
        assertFalse(command.accept("poloz 22"));
    }

    @Test
    public void execute() throws Exception {
        verifyPassedVariables("wyrzuc mlot bojowy 22", "mlot bojowy", 22);
        verifyPassedVariables("porzuc mlot bojowy 22", "mlot bojowy", 22);
        verifyPassedVariables("upusc mlot bojowy 22", "mlot bojowy", 22);
        verifyPassedVariables("poloz mlot bojowy 22", "mlot bojowy", 22);
        verifyPassedVariables("wyrzuc 22 mlot bojowy", "mlot bojowy", 22);
        verifyPassedVariables("porzuc 22 mlot bojowy", "mlot bojowy", 22);
        verifyPassedVariables("upusc 22 mlot bojowy", "mlot bojowy", 22);
        verifyPassedVariables("poloz 22 mlot bojowy", "mlot bojowy", 22);
        verifyPassedVariables("wyrzuc mlot bojowy", "mlot bojowy", 1);
        verifyPassedVariables("porzuc mlot bojowy", "mlot bojowy", 1);
        verifyPassedVariables("upusc mlot bojowy", "mlot bojowy", 1);
        verifyPassedVariables("poloz mlot bojowy", "mlot bojowy", 1);
    }

    private void verifyPassedVariables(String userCommand, String expectedItemName, int expectedAmount) {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();

        reset(commandExecutor);
        command.execute(userCommand, player, response);
        verify(commandExecutor).drop(expectedItemName, expectedAmount, player, response);
        verifyNoMoreInteractions(commandExecutor);
    }
}