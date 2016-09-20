package org.grizz.game.command.parsers.admin;

import org.grizz.game.command.executors.admin.AdminShowPlayerListCommandExecutor;
import org.grizz.game.command.parsers.CommandParser;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AdminShowPlayerListCommand extends CommandParser {

    @Autowired
    private AdminShowPlayerListCommandExecutor adminCommand;

    @Autowired
    public AdminShowPlayerListCommand(Environment env) {
        super(env);
    }

    @Override
    public boolean accept(String command) {
        return isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, Player admin, PlayerResponse response) {
        adminCommand.showPlayerList(admin, response);
        return response;
    }
}
