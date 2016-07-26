package old.org.grizz.game.commands.impl;

import old.org.grizz.game.commands.Command;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.simple.EventService;
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
        response.getPlayerEvents().add(eventService.getEvent("unknown.command.invoked", command));
        return response;
    }
}