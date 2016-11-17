package org.grizz.game.command.parsers.admin;

import org.grizz.game.command.executors.admin.AdminShowActivePlayerListCommandExecutor;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminShowActivePlayerListCommandTest {
    @Mock
    private Environment environment;
    @Mock
    private AdminShowActivePlayerListCommandExecutor adminCommandExecutor;

    @InjectMocks
    private AdminShowActivePlayerListCommand command = new AdminShowActivePlayerListCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("lista graczy;" +
                        "lista graczy (?<lastMin>[\\d]+);" +
                        "lista graczy z ostatnich (?<lastMin>[\\d]+);" +
                        "lista graczy z ostatnich (?<lastMin>[\\d]+) min");
    }

    @Test
    public void testAccept() throws Exception {
        assertTrue(command.accept("lista graczy"));
        assertTrue(command.accept("lista graczy 30"));
        assertTrue(command.accept("lista graczy z ostatnich 30"));
        assertTrue(command.accept("lista graczy z ostatnich 30 min"));
    }

    @Test
    public void testExecute() throws Exception {
        verifyPassedVariables("lista graczy", 60);
        verifyPassedVariables("lista graczy 30", 30);
        verifyPassedVariables("lista graczy z ostatnich 30", 30);
        verifyPassedVariables("lista graczy z ostatnich 30 min", 30);
    }

    private void verifyPassedVariables(String userCommand, long expectedMinutes) {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();

        reset(adminCommandExecutor);
        command.execute(userCommand, player, response);
        verify(adminCommandExecutor).showPlayerList(expectedMinutes, player, response);
        verifyNoMoreInteractions(adminCommandExecutor);
    }
}