package org.grizz.game.command.parsers.admin;

import org.grizz.game.command.executors.admin.AdminPutItemCommandExecutor;
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
public class AdminPutItemCommandTest {
    @Mock
    private Environment environment;
    @Mock
    private AdminPutItemCommandExecutor adminCommandExecutor;

    @InjectMocks
    private AdminPutItemCommand command = new AdminPutItemCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("poloz (?<amount>[\\d]+) (?<itemName>[\\D]+);" +
                        "poloz (?<itemName>[\\D]+)");
    }

    @Test
    public void testAccept() throws Exception {
        assertTrue(command.accept("poloz mlot bojowy"));
        assertTrue(command.accept("poloz 2 mlot bojowy"));
        assertTrue(command.accept("poloz 22 mlot bojowy"));

        assertFalse(command.accept("poloz"));
        assertFalse(command.accept("poloz 2"));
        assertFalse(command.accept("poloz 2 22 mlot bojowy"));
    }

    @Test
    public void testExecute() throws Exception {
        verifyPassedVariables("poloz mlot bojowy", "mlot bojowy", 1);
        verifyPassedVariables("poloz 2 mlot bojowy", "mlot bojowy", 2);
        verifyPassedVariables("poloz 22 mlot bojowy", "mlot bojowy", 22);
    }

    private void verifyPassedVariables(String userCommand, String expectedItemName, int expectedAmount) {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();

        reset(adminCommandExecutor);
        command.execute(userCommand, player, response);
        verify(adminCommandExecutor).put(expectedItemName, expectedAmount, player, response);
        verifyNoMoreInteractions(adminCommandExecutor);
    }
}