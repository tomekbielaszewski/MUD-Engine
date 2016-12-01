package org.grizz.game.exception;

import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.EventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameExceptionHandlerTest {
    private static final String EXCEPTION_MESSAGE_CODE = "some.exception";
    private static final String[] EXCEPTION_PARAMS = {"some", "params"};
    private static final String EXCEPTION_MESSAGE = "Formatted exception message";

    @Mock
    private EventService eventService;

    @InjectMocks
    private GameExceptionHandler exceptionHandler = new GameExceptionHandler();

    @Test
    public void handlesExceptionAndPassesTheOutcomeToResponse() throws Exception {
        PlayerResponse response = new PlayerResponse();
        GameException exception = new PlayerDoesNotExistException(EXCEPTION_MESSAGE_CODE, EXCEPTION_PARAMS);
        when(eventService.getEvent(EXCEPTION_MESSAGE_CODE, EXCEPTION_PARAMS)).thenReturn(EXCEPTION_MESSAGE);

        exceptionHandler.handle(exception, response);

        assertThat(response.getPlayerEvents(), hasSize(1));
        assertThat(response.getPlayerEvents(), hasItem(EXCEPTION_MESSAGE));
    }

    @Test
    public void handlesAlreadyLocalizedExceptionWithoutUseOfEventService() throws Exception {
        PlayerResponse response = new PlayerResponse();
        GameException exception = new GameScriptException(EXCEPTION_MESSAGE);

        exceptionHandler.handleLocalized(exception, response);

        assertThat(response.getPlayerEvents(), hasSize(1));
        assertThat(response.getPlayerEvents(), hasItem(EXCEPTION_MESSAGE));
        verify(eventService, never()).getEvent(any());
    }
}