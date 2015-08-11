package org.grizz.game.commands.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Grizz on 2015-08-08.
 */
@Slf4j
@Component
public class PickUpCommand implements Command {
    @Autowired
    private Environment env;

    @Override
    public boolean accept(String command) {
        try {
            getMatching(command);
            log.info("Command [{}] accepted!", command);
            return true;
        } catch (NoSuchElementException e) {
            //expected when no pattern matches given command
        }
        return false;
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();
        Map.Entry<String, String> matching = getMatching(command);
        Pattern pattern = Pattern.compile(matching.getKey());
        Matcher matcher = pattern.matcher(matching.getValue());
        matcher.matches();
        int capturingGroups = matcher.groupCount();
        if (capturingGroups == 1) {
            String itemName = matcher.group(1);
            doSinglePickup(itemName, playerContext, response);
        } else if (capturingGroups == 2) {
            String itemName = matcher.group(1);
            Integer amount = Integer.valueOf(matcher.group(2));
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
        log.info("Multi pickup works! Item name is: [" + itemName + "] with amount of " + amount);
    }

    private Map.Entry<String, String> getMatching(String command) {
        String commandsStr = env.getProperty(getClass().getCanonicalName());
        ArrayList<String> patterns = Lists.newArrayList(commandsStr.split(";"));
        Map<String, String> matchingCommands = patterns.stream()
                .filter(pattern -> command.matches(pattern))
                .collect(Collectors.toMap(Function.<String>identity(), s -> command));
        return matchingCommands.entrySet().stream().findFirst().get();
    }
}
