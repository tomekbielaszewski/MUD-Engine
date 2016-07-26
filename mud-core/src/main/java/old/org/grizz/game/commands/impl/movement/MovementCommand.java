package old.org.grizz.game.commands.impl.movement;

import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.commands.Command;
import old.org.grizz.game.exception.GameExceptionHandler;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.model.enums.Direction;
import old.org.grizz.game.service.complex.MovementService;
import old.org.grizz.game.service.utils.CommandUtils;

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

    public PlayerResponse execute(Direction direction, final PlayerContext playerContext, PlayerResponse response) {
        movementService.moveRunningScripts(direction, playerContext, response);
        return response;
    }
}
