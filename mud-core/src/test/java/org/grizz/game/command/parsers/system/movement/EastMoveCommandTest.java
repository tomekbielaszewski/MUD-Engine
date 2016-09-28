package org.grizz.game.command.parsers.system.movement;

import org.grizz.game.command.executors.system.MoveCommandExecutor;
import org.grizz.game.model.Direction;
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
public class EastMoveCommandTest {
    @Mock
    private Environment environment;
    @Mock
    private MoveCommandExecutor commandExecutor;

    @InjectMocks
    private EastMoveCommand command = new EastMoveCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("east;" +
                        "wschod;" +
                        "idz na wschod");
    }

    @Test
    public void testAccept() throws Exception {
        assertTrue(command.accept("east"));
        assertTrue(command.accept("wschod"));
        assertTrue(command.accept("idz na wschod"));
    }

    @Test
    public void testExecute() throws Exception {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();

        command.execute("east", player, response);

        verify(commandExecutor).move(Direction.EAST, player, response);
    }
}