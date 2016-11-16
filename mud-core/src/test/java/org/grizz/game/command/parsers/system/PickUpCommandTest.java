package org.grizz.game.command.parsers.system;

import org.grizz.game.command.executors.PickUpCommandExecutor;
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
public class PickUpCommandTest {

    @Mock
    private PickUpCommandExecutor commandExecutor;
    @Mock
    private Environment environment;

    @InjectMocks
    private PickUpCommand command = new PickUpCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("podnies (?<itemName>[\\D]+) (?<amount>[\\d]+);" +
                        "wez (?<itemName>[\\D]+) (?<amount>[\\d]+);" +
                        "podnies (?<itemName>[\\D]+);" +
                        "wez (?<itemName>[\\D]+);" +
                        "podnies (?<amount>[\\d]+) (?<itemName>[\\D]+);" +
                        "wez (?<amount>[\\d]+) (?<itemName>[\\D]+)");
    }

    @Test
    public void accept() throws Exception {
        assertTrue(command.accept("wez mlot bojowy"));
        assertTrue(command.accept("podnies mlot bojowy"));
        assertTrue(command.accept("wez 22 mlot bojowy"));
        assertTrue(command.accept("podnies 22 mlot bojowy"));
        assertTrue(command.accept("wez mlot bojowy 22"));
        assertTrue(command.accept("podnies mlot bojowy 22"));

        assertFalse(command.accept("podnies "));
        assertFalse(command.accept("podnies 22"));
        assertFalse(command.accept("wez "));
        assertFalse(command.accept("wez 22"));
    }

    @Test
    public void execute() throws Exception {
        verifyPassedVariables("wez mlot bojowy", "mlot bojowy", 1);
        verifyPassedVariables("podnies mlot bojowy", "mlot bojowy", 1);
        verifyPassedVariables("wez 22 mlot bojowy", "mlot bojowy", 22);
        verifyPassedVariables("podnies 22 mlot bojowy", "mlot bojowy", 22);
        verifyPassedVariables("wez mlot bojowy 22", "mlot bojowy", 22);
        verifyPassedVariables("podnies mlot bojowy 22", "mlot bojowy", 22);
    }

    private void verifyPassedVariables(String userCommand, String expectedItemName, int expectedAmount) {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();

        reset(commandExecutor);
        command.execute(userCommand, player, response);
        verify(commandExecutor).pickUp(expectedItemName, expectedAmount, player, response);
        verifyNoMoreInteractions(commandExecutor);
    }
}