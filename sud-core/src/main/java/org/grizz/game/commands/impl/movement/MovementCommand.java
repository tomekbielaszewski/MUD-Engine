package org.grizz.game.commands.impl.movement;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.commands.Command;
import org.grizz.game.exception.GameException;
import org.grizz.game.exception.GameExceptionHandler;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.enums.Direction;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.service.complex.MovementService;
import org.grizz.game.service.utils.CommandUtils;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@Slf4j
public abstract class MovementCommand implements Command {
    protected CommandUtils commandUtils;
    protected GameExceptionHandler exceptionHandler;
    protected MovementService movementService;

    public MovementCommand(CommandUtils commandUtils, GameExceptionHandler exceptionHandler, MovementService movementService) {
        this.commandUtils = commandUtils;
        this.exceptionHandler = exceptionHandler;
        this.movementService = movementService;
    }

    @Override
    public boolean accept(final String command) {
        return commandUtils.isAnyMatching(command, getClass().getCanonicalName());
    }

    public PlayerResponse execute(Direction direction, final PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();
        try {
            movementService.move(direction, playerContext, response);
        } catch (GameException e) {
            String formattedEvent = exceptionHandler.handle(e);
            response.getPlayerEvents().add(formattedEvent);
        }

        return response;
    }
}
