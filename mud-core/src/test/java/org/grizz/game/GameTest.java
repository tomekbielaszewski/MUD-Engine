package org.grizz.game;

import org.grizz.game.command.engine.CommandHandler;
import org.grizz.game.exception.GameException;
import org.grizz.game.exception.GameExceptionHandler;
import org.grizz.game.exception.PlayerDoesNotExist;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {
    private static final String NAME = "name";
    private static final String COMMAND = "command";

    @Mock
    private CommandHandler commandHandler;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private GameExceptionHandler exceptionHandler;

    @InjectMocks
    private Game game = new Game();

    @Test
    public void callsCommandHandlerWithFoundPlayer() {
        Player player = dummyPlayer();
        when(playerRepository.findByName(NAME)).thenReturn(player);

        game.runCommand(COMMAND, NAME);

        verify(commandHandler).execute(eq(COMMAND), eq(player), any());
        verify(playerRepository).save(player);
    }

    @Test
    public void callsExceptionHandlerWhenErrorThrownFromCommandHandler() {
        Player player = dummyPlayer();
        when(playerRepository.findByName(NAME)).thenReturn(player);
        when(commandHandler.execute(eq(COMMAND), eq(player), any())).thenThrow(new GameException(""));

        PlayerResponse response = game.runCommand(COMMAND, NAME);

        verify(exceptionHandler).handle(any(GameException.class), eq(response));
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void callsExceptionHandlerWhenPlayerNotFound() {
        when(playerRepository.findByName(NAME)).thenReturn(null);

        PlayerResponse response = game.runCommand(COMMAND, NAME);

        verify(exceptionHandler).handle(any(PlayerDoesNotExist.class), eq(response));
        verify(playerRepository, never()).save(any(Player.class));
    }

    private Player dummyPlayer() {
        return Player.builder().build();
    }
}