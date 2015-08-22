package org.grizz.game.commands.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.commands.Command;
import org.grizz.game.exception.GameException;
import org.grizz.game.exception.GameExceptionHandler;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.service.complex.PlayerLocationInteractionService;
import org.grizz.game.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Grizz on 2015-08-08.
 */
@Slf4j
@Component
public class DropCommand implements Command {
    @Autowired
    private Environment env;

    @Autowired
    private GameExceptionHandler exceptionHandler;

    @Autowired
    private PlayerLocationInteractionService playerLocationInteractionService;

    @Override
    public boolean accept(String command) {
        long amountOfMatchedCommands = getCommandPatterns().stream()
                .filter(pattern -> CommandUtils.isMatching(command, pattern))
                .count();

        return amountOfMatchedCommands > 0;
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();

        String[] commandSplit = CommandUtils.splitCommand(command, getMatchedPattern(command));

        if (commandSplit.length == 1) {
            String itemName = commandSplit[0];
            doSingleDrop(itemName, playerContext, response);
        } else if (commandSplit.length == 2) {
            String itemName = commandSplit[0];
            Integer amount = Integer.valueOf(commandSplit[1]);
            doMultiDrop(itemName, amount, playerContext, response);
        } else {
            throw new IllegalArgumentException("There is an error in pattern matching command []! " +
                    "To many or zero capturing groups!");
        }

        return response;
    }

    private void doSingleDrop(String itemName, PlayerContext playerContext, PlayerResponse response) {
        doMultiDrop(itemName, 1, playerContext, response);
    }

    private void doMultiDrop(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        try {
            playerLocationInteractionService.dropItems(itemName, amount, playerContext, response);
            log.info("{} dropped {} of {}", playerContext.getName(), amount, itemName);
        } catch (GameException e) {
            String formattedEvent = exceptionHandler.handle(e);
            response.getPlayerEvents().add(formattedEvent);
        }
    }

    private String getMatchedPattern(String command) {
        String matchedPattern = getCommandPatterns().stream()
                .filter(pattern -> command.matches(pattern))
                .findFirst().get();
        return matchedPattern;
    }

    private List<String> getCommandPatterns() {
        String[] commandPatternsArray = env.getProperty(getClass().getCanonicalName())
                .split(";");
        return Lists.newArrayList(commandPatternsArray);
    }
}
