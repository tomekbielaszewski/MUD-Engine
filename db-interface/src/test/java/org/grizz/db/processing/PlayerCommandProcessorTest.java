package org.grizz.db.processing;

import com.google.common.collect.Lists;
import org.grizz.db.model.PlayerCommand;
import org.grizz.db.model.ProcessedPlayerResponse;
import org.grizz.db.model.RawPlayerResponse;
import org.grizz.game.Game;
import org.grizz.game.model.PlayerResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PlayerCommandProcessorTest {
    private static final String COMMAND_STRING = "command";
    private static final String PLAYER_NAME = "player name";
    private static final PlayerCommand COMMAND = dummyCommand();
    private static final PlayerResponse RESPONSE = dummyResponse();
    private static final String PLAYER_EVENT = "some event";
    private static final RawPlayerResponse RAW_RESPONSE = dummyRawResponse();

    @Mock
    private Game game;

    @Mock
    private ResponseCollector responseCollector;

    @Mock
    private PlayerResponseFormatter playerResponseFormatter;

    @InjectMocks
    private PlayerCommandProcessor playerCommandProcessor = new PlayerCommandProcessor();

    @Test
    public void runsCommandInGame() throws Exception {
        playerCommandProcessor.process(COMMAND);

        verify(game).runCommand(COMMAND_STRING, PLAYER_NAME);
    }

    @Test
    public void collectsResponseFromGame() throws Exception {
        when(game.runCommand(COMMAND_STRING, PLAYER_NAME)).thenReturn(RESPONSE);

        playerCommandProcessor.process(COMMAND);

        verify(responseCollector).collect(PLAYER_NAME, RESPONSE);
    }

    @Test
    public void shouldMarkCommandAsProcessed() throws Exception {
        PlayerCommand playerCommand = dummyCommand();

        playerCommandProcessor.process(playerCommand);

        assertThat(playerCommand.isProcessed(), is(true));
    }

    @Test
    public void returnsProcessedResponse() throws Exception {
        when(responseCollector.getRawResponses()).thenReturn(Lists.newArrayList(RAW_RESPONSE));
        when(playerResponseFormatter.format(PLAYER_NAME, COMMAND_STRING, RESPONSE)).thenReturn(PLAYER_EVENT);

        List<ProcessedPlayerResponse> processed = playerCommandProcessor.process(COMMAND);

        assertThat(processed, hasSize(1));
        ProcessedPlayerResponse response = processed.get(0);
        assertThat(response.getPlayerCommand(), is(COMMAND));
        assertThat(response.getReceiver(), is(PLAYER_NAME));
        assertThat(response.isSent(), is(false));
        assertThat(response.getResponse(), is(PLAYER_EVENT));
    }

    private static PlayerCommand dummyCommand() {
        return PlayerCommand.builder().command(COMMAND_STRING).player(PLAYER_NAME).build();
    }

    private static PlayerResponse dummyResponse() {
        PlayerResponse playerResponse = new PlayerResponse();
        playerResponse.setPlayerEvents(Lists.newArrayList(PLAYER_EVENT));
        return playerResponse;
    }

    private static RawPlayerResponse dummyRawResponse() {
        return RawPlayerResponse.builder().playerName(PLAYER_NAME).response(RESPONSE).build();
    }
}