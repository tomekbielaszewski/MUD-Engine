package org.grizz.game.command.parsers.admin;

import org.grizz.game.command.executors.admin.AdminGiveItemCommandExecutor;
import org.grizz.game.command.parsers.CommandParser;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AdminGiveItemCommand extends CommandParser {

    @Autowired
    private AdminGiveItemCommandExecutor adminCommand;

    @Autowired
    public AdminGiveItemCommand(Environment env) {
        super(env);
    }

    @Override
    public boolean accept(String command) {
        return isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, Player admin, PlayerResponse response) {
        String matchedPattern = getMatchedPattern(command, getClass().getCanonicalName());

        String itemName = getVariable("itemName", command, matchedPattern);
        String amountStr = getVariableOrDefaultValue("amount", "1", command, matchedPattern);
        String playerName = getVariableOrDefaultValue("playerName", admin.getName(), command, matchedPattern);

        int amount = Integer.parseInt(amountStr);

        adminCommand.give(playerName, itemName, amount, admin, response);

        return response;
    }
}
