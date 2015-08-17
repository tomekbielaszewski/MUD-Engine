package org.grizz.game.commands.impl;

import com.google.common.collect.Lists;
import org.grizz.game.commands.Command;
import org.grizz.game.exception.GameException;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.service.MovementService;
import org.grizz.game.utils.GameExceptionHandler;
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
    private GameExceptionHandler exceptionHandler;

    @Autowired
    private MovementService movementService;

    @Override
    public boolean accept(final String command) {
        String commands = env.getProperty(getClass().getCanonicalName());
        return Lists.newArrayList(commands.split(";")).contains(command);
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
        } catch (GameException e) {
            String formattedEvent = exceptionHandler.handle(e);
            response.getPlayerEvents().add(formattedEvent);
        }

        return response;
    }
}
