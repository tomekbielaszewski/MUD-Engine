package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.exception.CantGoThereException;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.grizz.game.model.enums.Direction.*;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@Component
public class MovementCommand implements Command {
    @Autowired
    private Environment env;

    @Autowired
    private MovementService movementService;

    @Override
    public boolean accept(final String command) {
        return "south".equals(command) ||
                "north".equals(command) ||
                "east".equals(command) ||
                "west".equals(command) ||
                "up".equals(command) ||
                "down".equals(command);
    }

    @Override
    public PlayerResponse execute(final String command, final PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();
        try {
            switch (command) {
                case "north":
                    movementService.move(NORTH, playerContext, response);
                    break;
                case "south":
                    movementService.move(SOUTH, playerContext, response);
                    break;
                case "east":
                    movementService.move(EAST, playerContext, response);
                    break;
                case "west":
                    movementService.move(WEST, playerContext, response);
                    break;
                case "up":
                    movementService.move(UP, playerContext, response);
                    break;
                case "down":
                    movementService.move(DOWN, playerContext, response);
                    break;
            }
        } catch (CantGoThereException e) {
            String event = env.getProperty("cant.go.there");
            response.getPlayerEvents().add(event);
        }

        return response;
    }
}
