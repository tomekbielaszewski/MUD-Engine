package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.exception.CantGoThereException;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.grizz.game.service.Direction.*;

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
        try {
            switch (command) {
                case "north":
                    movementService.move(NORTH, playerContext);
                    break;
                case "south":
                    movementService.move(SOUTH, playerContext);
                    break;
                case "east":
                    movementService.move(EAST, playerContext);
                    break;
                case "west":
                    movementService.move(WEST, playerContext);
                    break;
                case "up":
                    movementService.move(UP, playerContext);
                    break;
                case "down":
                    movementService.move(DOWN, playerContext);
                    break;
            }
        } catch (CantGoThereException e) {
            //TODO: add event to response not context
            playerContext.addEvent(env.getProperty("cant.go.there"));
        }

        return null; //TODO: return PlayerResponse
    }
}
