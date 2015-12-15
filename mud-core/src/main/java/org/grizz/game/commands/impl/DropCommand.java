package org.grizz.game.commands.impl;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.commands.Command;
import org.grizz.game.exception.GameExceptionHandler;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.complex.PlayerLocationInteractionService;
import org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Grizz on 2015-08-08.
 */
@Slf4j
@Component
public class DropCommand implements Command {
    @Autowired
    private GameExceptionHandler exceptionHandler;

    @Autowired
    private CommandUtils commandUtils;

    @Autowired
    private PlayerLocationInteractionService playerLocationInteractionService;

    @Override
    public boolean accept(String command) {
        return commandUtils.isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext, PlayerResponse response) {
        String matchedPattern = commandUtils.getMatchedPattern(command, getClass().getCanonicalName());

        String itemName = commandUtils.getVariable("itemName", command, matchedPattern);
        String amountStr = commandUtils.getVariableOrDefaultValue("amount", "1", command, matchedPattern);

        Integer amount = Integer.valueOf(amountStr);

        playerLocationInteractionService.dropItems(itemName, amount, playerContext, response);
        log.info("{} dropped {} of {}", playerContext.getName(), amount, itemName);

        return response;
    }
}
