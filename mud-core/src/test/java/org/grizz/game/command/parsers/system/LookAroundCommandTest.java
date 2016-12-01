package org.grizz.game.command.parsers.system;

import org.grizz.game.command.executors.system.LookAroundCommandExecutor;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LookAroundCommandTest {
    @Mock
    private Environment environment;
    @Mock
    private LookAroundCommandExecutor commandExecutor;

    @InjectMocks
    private LookAroundCommand command = new LookAroundCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("spojrz;" +
                        "rozejrz sie;" +
                        "rozejrzyj sie;" +
                        "gdzie jestem\\?;" +
                        "gdzie ja jestem\\?;" +
                        "gdzie jestem;" +
                        "gdzie ja jestem;" +
                        "co to za miejsce\\?;" +
                        "co to za miejsce;" +
                        "lokalizacja");
    }

    @Test
    public void testAccept() throws Exception {
        assertTrue(command.accept("spojrz"));
        assertTrue(command.accept("rozejrz sie"));
        assertTrue(command.accept("rozejrzyj sie"));
        assertTrue(command.accept("gdzie jestem?"));
        assertTrue(command.accept("gdzie ja jestem?"));
        assertTrue(command.accept("gdzie jestem"));
        assertTrue(command.accept("gdzie ja jestem"));
        assertTrue(command.accept("co to za miejsce?"));
        assertTrue(command.accept("co to za miejsce"));
        assertTrue(command.accept("lokalizacja"));
    }

    @Test
    public void testExecute() throws Exception {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();

        command.execute("spojrz", player, response);

        verify(commandExecutor).lookAround(player, response);
    }
}