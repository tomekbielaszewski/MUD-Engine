package org.grizz.game.command.parsers.admin;

import org.grizz.game.command.executors.admin.AdminShowActivePlayerListCommandExecutor;
import org.grizz.game.command.parsers.CommandParser;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AdminShowActivePlayerListCommand extends CommandParser {

    @Autowired
    private AdminShowActivePlayerListCommandExecutor adminCommand;

    @Autowired
    public AdminShowActivePlayerListCommand(Environment env) {
        super(env);
    }

    @Override
    public boolean accept(String command) {
        return isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, Player admin, PlayerResponse response) {
        String matchedPattern = getMatchedPattern(command, getClass().getCanonicalName());

        String lastMinutesStr = getVariableOrDefaultValue("lastMin", "60", command, matchedPattern);
        int lastMinutes = Integer.parseInt(lastMinutesStr);

        adminCommand.showPlayerList(lastMinutes, admin, response);
        return response;
    }
}