package org.grizz.game.command.parsers.system;

import org.grizz.game.command.executors.system.PickUpCommandExecutor;
import org.grizz.game.command.parsers.CommandParser;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PickUpCommand extends CommandParser {
    @Autowired
    private PickUpCommandExecutor commandExecutor;

    @Autowired
    public PickUpCommand(Environment env) {
        super(env);
    }

    @Override
    public boolean accept(String command) {
        return isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, Player player, PlayerResponse response) {
        String matchedPattern = getMatchedPattern(command, getClass().getCanonicalName());

        String itemName = getVariable("itemName", command, matchedPattern);
        String amountStr = getVariableOrDefaultValue("amount", "1", command, matchedPattern);
        int amount = Integer.parseInt(amountStr);

        commandExecutor.pickUp(itemName, amount, player, response);

        return response;
    }
}
