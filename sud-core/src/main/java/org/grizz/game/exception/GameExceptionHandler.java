package org.grizz.game.exception;

import org.grizz.game.service.simple.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Grizz on 2015-08-17.
 */
@Component
public class GameExceptionHandler {
    @Autowired
    private EventService eventService;

    public String handle(GameException e) {
        String message = e.getMessage();
        return eventService.getEvent(message, e.getParams());
    }
}
