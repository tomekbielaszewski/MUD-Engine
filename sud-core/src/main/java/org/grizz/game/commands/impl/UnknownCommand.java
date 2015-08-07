package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by tomasz.bielaszewski on 2015-05-05.
 */
@Component
public class UnknownCommand implements Command {
    @Autowired
    private Environment env;

    @Override
    public boolean accept(String command) {
        return true;
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext context) {
        PlayerResponse response = new PlayerResponseImpl();

        String event = env.getProperty("unknown.command.invoced") + " \"" + command + "\"";
        response.getPlayerEvents().add(event);

        return response;
    }
}