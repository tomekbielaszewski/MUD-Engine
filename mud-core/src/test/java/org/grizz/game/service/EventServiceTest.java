package org.grizz.game.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventServiceTest {
    private static final String EVENT_PROPERTY = "event.property";
    private static final String UNFORMATTED_EVENT = "Unformatted event with two properties %s %s";
    private static final String[] EVENT_PARAMS = {"first", "second"};
    private static final String FORMATTED_EVENT = "Unformatted event with two properties first second";

    @Mock
    private Environment environment;

    @InjectMocks
    private EventService eventService = new EventService();

    @Test
    public void returnsFormattedEventWhenKeyExists() throws Exception {
        when(environment.getProperty(EVENT_PROPERTY)).thenReturn(UNFORMATTED_EVENT);

        String event = eventService.getEvent(EVENT_PROPERTY, EVENT_PARAMS);

        assertThat(event, is(FORMATTED_EVENT));
    }

    @Test
    public void returnsMessageKeyEventWhenKeyDoesNotExists() throws Exception {
        when(environment.getProperty(EVENT_PROPERTY)).thenReturn(null);

        String event = eventService.getEvent(EVENT_PROPERTY, EVENT_PARAMS);

        assertThat(event, is(EVENT_PROPERTY));
    }
}