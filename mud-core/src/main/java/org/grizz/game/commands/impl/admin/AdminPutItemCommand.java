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
public class AdminPutItemCommand implements Command {
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

        String itemName = commandUtils.getVariable("itemName", command, matchedPattern);
        String amountStr = commandUtils.getVariableOrDefaultValue("amount", "1", command, matchedPattern);

        int amount = Integer.parseInt(amountStr);

        administratorService.put(itemName, amount, playerContext, response);

        return response;
    }
}
