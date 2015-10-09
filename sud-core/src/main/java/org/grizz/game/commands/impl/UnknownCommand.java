package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.simple.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tomasz.bielaszewski on 2015-05-05.
 */
@Component
public class UnknownCommand implements Command {
    @Autowired
    private EventService eventService;

    @Override
    public boolean accept(String command) {
        return true;
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext context, PlayerResponse response) {
        response.getPlayerEvents().add(eventService.getEvent("unknown.command.invoced", command));
        return response;
    }
}