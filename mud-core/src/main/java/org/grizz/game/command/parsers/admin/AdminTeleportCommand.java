package org.grizz.game.command.parsers.admin;

import org.grizz.game.command.executors.AdminTeleportCommandExecutor;
import org.grizz.game.command.parsers.CommandParser;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AdminTeleportCommand extends CommandParser {

    @Autowired
    private AdminTeleportCommandExecutor adminCommand;

    @Autowired
    public AdminTeleportCommand(Environment env) {
        super(env);
    }

    @Override
    public boolean accept(String command) {
        return isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, Player admin, PlayerResponse response) {
        String matchedPattern = getMatchedPattern(command, getClass().getCanonicalName());

        String playerName = getVariable("playerName", command, matchedPattern);
        String locationId = getVariableOrDefaultValue("locationId", admin.getLocation(), command, matchedPattern);

        adminCommand.teleport(playerName, locationId, admin, response);

        return response;
    }
}
