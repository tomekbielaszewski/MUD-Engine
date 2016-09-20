package org.grizz.game;

import junit.framework.TestCase;
import org.grizz.game.command.engine.CommandHandler;
import org.grizz.game.model.Player;
import org.grizz.game.model.repository.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTest extends TestCase {
    private static final String NAME = "name";
    private static final String COMMAND = "command";

    @Mock
    private CommandHandler commandHandler;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private Game game = new Game();

    @Test
    public void callsCommandHandlerWithFoundPlayer() {
        Player player = dummyPlayer();
        when(playerRepository.findByName(NAME)).thenReturn(player);

        game.runCommand(COMMAND, NAME);

        verify(commandHandler).execute(eq(COMMAND), eq(player), any());
    }

    private Player dummyPlayer() {
        return Player.builder().build();
    }
}