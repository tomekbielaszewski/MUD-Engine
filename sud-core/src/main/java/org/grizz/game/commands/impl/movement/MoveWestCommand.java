package org.grizz.game.commands.impl.movement;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.GameExceptionHandler;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.complex.MovementService;
import org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.grizz.game.model.enums.Direction.WEST;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@Slf4j
@Component
public class MoveWestCommand extends MovementCommand {
    @Autowired
    public MoveWestCommand(CommandUtils commandUtils, GameExceptionHandler exceptionHandler, MovementService movementService) {
        super(commandUtils, exceptionHandler, movementService);
    }

    @Override
    public PlayerResponse execute(final String command, final PlayerContext playerContext) {
        return super.execute(WEST, playerContext);
    }
}
