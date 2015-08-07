package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@Component
public class LookAroundCommand implements Command {
    @Autowired
    private Environment env;

    @Autowired
    private MovementService movementService;

    @Override
    public boolean accept(final String command) {
        return "spojrz".equals(command) ||
                "rozejrz sie".equals(command) ||
                "gdzie jestem?".equals(command) ||
                "gdzie ja jestem?".equals(command) ||
                "gdzie jestem".equals(command) ||
                "gdzie ja jestem".equals(command) ||
                "co to za miejsce".equals(command) ||
                "co to za miejsce?".equals(command) ||
                "lokalizacja".equals(command);
    }

    @Override
    public PlayerResponse execute(final String command, final PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();
        movementService.showCurrentLocation(playerContext, response);
        return response;
    }
}
