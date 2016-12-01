package org.grizz.db.processing;

import com.google.common.collect.Lists;
import org.grizz.db.model.PlayerCommand;
import org.grizz.db.model.ProcessedPlayerResponse;
import org.grizz.db.model.repository.PlayerCommandRepository;
import org.grizz.db.model.repository.ProcessedPlayerResponseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandProcessingTaskTest {
    private static final PlayerCommand COMMAND = dummyCommand();
    private static final String COMMAND_STRING = "command";
    private static final String PLAYER_NAME = "player name";
    private static final ProcessedPlayerResponse PROCESSED_RESPONSE = dummyResponse();

    @Mock
    private PlayerCommandRepository playerCommandRepository;
    @Mock
    private ProcessedPlayerResponseRepository playerResponseRepository;
    @Mock
    private PlayerCommandProcessor commandProcessor;

    @InjectMocks
    private CommandProcessingTask task = new CommandProcessingTask();

    @Test
    public void processesUnprocessedCommandsAndSavesResponseAndChangedCommand() throws Exception {
        when(playerCommandRepository.findByProcessedOrderByTimestampAsc(false)).thenReturn(Lists.newArrayList(COMMAND));
        when(commandProcessor.process(COMMAND)).thenReturn(Lists.newArrayList(PROCESSED_RESPONSE));

        task.run();

        verify(playerResponseRepository).save(Lists.newArrayList(PROCESSED_RESPONSE));
        verify(playerCommandRepository).save(COMMAND);
    }

    private static PlayerCommand dummyCommand() {
        return PlayerCommand.builder().command(COMMAND_STRING).player(PLAYER_NAME).build();
    }

    private static ProcessedPlayerResponse dummyResponse() {
        return ProcessedPlayerResponse.builder().id("id").build();
    }
}