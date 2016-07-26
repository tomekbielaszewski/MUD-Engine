package old.org.grizz.game.commands.impl;

import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.commands.Command;
import old.org.grizz.game.exception.GameExceptionHandler;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.complex.PlayerLocationInteractionService;
import old.org.grizz.game.service.utils.CommandUtils;
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
    public PlayerResponse execute(String command, PlayerContext playerContext, PlayerResponse response) {
        String matchedPattern = commandUtils.getMatchedPattern(command, getClass().getCanonicalName());

        String itemName = commandUtils.getVariable("itemName", command, matchedPattern);
        String amountStr = commandUtils.getVariableOrDefaultValue("amount", "1", command, matchedPattern);

        Integer amount = Integer.valueOf(amountStr);

        playerLocationInteractionService.pickUpItems(itemName, amount, playerContext, response);
        log.info("{} picked up {} of {}", playerContext.getName(), amount, itemName);

        return response;
    }
}
