package old.org.grizz.game.commands.impl;

import old.org.grizz.game.commands.Command;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.complex.MovementService;
import old.org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@Component
public class LookAroundCommand implements Command {
    @Autowired
    private CommandUtils commandUtils;

    @Autowired
    private MovementService movementService;

    @Override
    public boolean accept(final String command) {
        return commandUtils.isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(final String command, final PlayerContext playerContext, PlayerResponse response) {
        movementService.showCurrentLocation(playerContext, response);
        return response;
    }
}
