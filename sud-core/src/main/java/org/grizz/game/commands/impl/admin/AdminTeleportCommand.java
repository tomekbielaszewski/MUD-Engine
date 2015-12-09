package org.grizz.game.commands.impl.admin;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.complex.AdministratorService;
import org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Grizz on 2015-11-30.
 */
@Component
public class AdminTeleportCommand implements Command {
    @Autowired
    private CommandUtils commandUtils;

    @Autowired
    private AdministratorService administratorService;

    @Override
    public boolean accept(String command) {
        return commandUtils.isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext, PlayerResponse response) {
        String matchedPattern = commandUtils.getMatchedPattern(command, getClass().getCanonicalName());
        String[] commandSplit = commandUtils.splitCommand(command, matchedPattern);

        if (commandSplit.length == 2) {
            String playerName = commandSplit[0];
            String locationId = commandSplit[1];

            administratorService.teleport(playerName, locationId, playerContext, response);
        } else if (commandSplit.length == 1) {
            String playerName = commandSplit[0];
            String locationId = playerContext.getCurrentLocation();

            administratorService.teleport(playerName, locationId, playerContext, response);
        } else {
            throw new IllegalArgumentException("There is an error in pattern matching command []! " +
                    "To many or zero capturing groups!");
        }

        return response;
    }
}
