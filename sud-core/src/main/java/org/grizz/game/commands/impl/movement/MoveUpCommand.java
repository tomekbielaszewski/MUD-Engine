package org.grizz.game.commands.impl.movement;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.GameExceptionHandler;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.grizz.game.model.enums.Direction.UP;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@Slf4j
@Component
public class MoveUpCommand extends MovementCommand {
    @Autowired
    public MoveUpCommand(Environment env, GameExceptionHandler exceptionHandler, MovementService movementService) {
        super(env, exceptionHandler, movementService);
    }

    @Override
    public PlayerResponse execute(final String command, final PlayerContext playerContext) {
        return super.execute(UP, playerContext);
    }
}
