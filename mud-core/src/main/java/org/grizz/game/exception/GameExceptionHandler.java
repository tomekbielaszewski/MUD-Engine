package org.grizz.game.exception;

import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameExceptionHandler {
    @Autowired
    private EventService eventService;

    public void handle(GameException e, PlayerResponse playerResponse) {
        String message = e.getMessage();
        String event = eventService.getEvent(message, e.getParams());
        playerResponse.getPlayerEvents().add(event);
    }

    public void handleLocalized(GameException e, PlayerResponse playerResponse) {
        String message = e.getMessage();
        playerResponse.getPlayerEvents().add(message);
    }
}
