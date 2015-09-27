package org.grizz.game.commands.impl;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.commands.Command;
import org.grizz.game.exception.GameException;
import org.grizz.game.exception.GameExceptionHandler;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.service.complex.PlayerLocationInteractionService;
import org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Grizz on 2015-08-08.
 */
@Slf4j
@Component
public class PickUpCommand implements Command {
    @Autowired
    private CommandUtils commandUtils;

    @Autowired
    private GameExceptionHandler exceptionHandler;

    @Autowired
    private PlayerLocationInteractionService playerLocationInteractionService;

    @Override
    public boolean accept(String command) {
        return commandUtils.isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();

        String matchedPattern = commandUtils.getMatchedPattern(command, getClass().getCanonicalName());
        String[] commandSplit = commandUtils.splitCommand(command, matchedPattern);

        if (commandSplit.length == 1) {
            String itemName = commandSplit[0];
            doSinglePickup(itemName, playerContext, response);
        } else if (commandSplit.length == 2) {
            String itemName = commandSplit[0];
            Integer amount = Integer.valueOf(commandSplit[1]);
            doMultiPickup(itemName, amount, playerContext, response);
        } else {
            throw new IllegalArgumentException("There is an error in pattern matching command []! " +
                    "To many or zero capturing groups!");
        }

        return response;
    }

    private void doSinglePickup(String itemName, PlayerContext playerContext, PlayerResponse response) {
        doMultiPickup(itemName, 1, playerContext, response);
    }

    private void doMultiPickup(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        try {
            playerLocationInteractionService.pickUpItems(itemName, amount, playerContext, response);
            log.info("{} picked up {} of {}", playerContext.getName(), amount, itemName);
        } catch (GameException e) {
            String formattedEvent = exceptionHandler.handle(e);
            response.getPlayerEvents().add(formattedEvent);
        }
    }
}
