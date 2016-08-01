package org.grizz.game.command.engine;

import com.google.common.collect.Lists;
import junit.framework.TestCase;
import org.grizz.game.command.Command;
import org.grizz.game.command.provider.CommandsProvider;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandHandlerTest extends TestCase {
    private final String command = "command";
    private final Player player = dummyPlayer();
    private final PlayerResponse response = dummyResponse();

    @Spy
    private final Command matching = matchingCommand();

    @Spy
    private final Command notMatching = notMatchingCommand();

    @Mock
    private CommandsProvider commandsProvider;

    @Mock
    private Command unknownCommand;

    @InjectMocks
    private CommandHandler commandHandler = new CommandHandler();

    @Test
    public void executesFirstCommandWhenMatching() {
        when(commandsProvider.provide(player)).thenReturn(Lists.newArrayList(matching, notMatching));

        commandHandler.execute(command, player, response);

        verify(matching).execute(command, player, response);
        verify(notMatching, never()).execute(any(), any(), any());
        verify(unknownCommand, never()).execute(any(), any(), any());
    }

    @Test
    public void executesSecondCommandWhenMatchingAndFirstNot() {
        when(commandsProvider.provide(player)).thenReturn(Lists.newArrayList(notMatching, matching));

        commandHandler.execute(command, player, response);

        verify(matching).execute(command, player, response);
        verify(notMatching, never()).execute(any(), any(), any());
        verify(unknownCommand, never()).execute(any(), any(), any());
    }

    @Test
    public void executesUnknownCommandWhenNoCommandsMatching() {
        when(commandsProvider.provide(player)).thenReturn(Lists.newArrayList(notMatching, notMatching));

        commandHandler.execute(command, player, response);

        verify(matching, never()).execute(any(), any(), any());
        verify(notMatching, never()).execute(any(), any(), any());
        verify(unknownCommand).execute(command, player, response);
    }

    @Test
    public void executesUnknownCommandWhenNoCommandsReturnedFromProvider() {
        when(commandsProvider.provide(player)).thenReturn(Lists.newArrayList());

        commandHandler.execute(command, player, response);

        verify(unknownCommand).execute(command, player, response);

    }

    private Player dummyPlayer() {
        return new Player();
    }

    private PlayerResponse dummyResponse() {
        return new PlayerResponse();
    }

    private Command matchingCommand() {
        return new Command() {
            @Override
            public boolean accept(String command) {
                return true;
            }

            @Override
            public PlayerResponse execute(String command, Player player, PlayerResponse response) {
                return null;
            }
        };
    }

    private Command notMatchingCommand() {
        return new Command() {
            @Override
            public boolean accept(String command) {
                return false;
            }

            @Override
            public PlayerResponse execute(String command, Player player, PlayerResponse response) {
                return null;
            }
        };
    }
}