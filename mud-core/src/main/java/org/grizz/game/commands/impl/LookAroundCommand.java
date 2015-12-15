package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.complex.MovementService;
import org.grizz.game.service.utils.CommandUtils;
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
