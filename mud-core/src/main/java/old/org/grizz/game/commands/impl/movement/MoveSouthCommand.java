package old.org.grizz.game.commands.impl.movement;

import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.exception.GameExceptionHandler;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.model.enums.Direction;
import old.org.grizz.game.service.complex.MovementService;
import old.org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@Slf4j
@Component
public class MoveSouthCommand extends MovementCommand {
    @Autowired
    public MoveSouthCommand(CommandUtils commandUtils, GameExceptionHandler exceptionHandler, MovementService movementService) {
        super(commandUtils, exceptionHandler, movementService);
    }

    @Override
    public PlayerResponse execute(final String command, final PlayerContext playerContext, PlayerResponse response) {
        return super.execute(Direction.SOUTH, playerContext, response);
    }
}