package org.grizz.game.commands.impl;

import com.google.common.collect.Lists;
import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Grizz on 2015-08-08.
 */
@Component
public class PickUpCommand implements Command {
    @Autowired
    private Environment env;

    @Override
    public boolean accept(String command) {
        String commandsStr = env.getProperty(getClass().getCanonicalName());
        ArrayList<String> commands = Lists.newArrayList(commandsStr.split(";"));
        List<String> matchedCommands = commands.stream()
                .filter(comm -> command.matches(comm))
                .collect(Collectors.toList());
        return !matchedCommands.isEmpty();
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext) {

        System.out.println("DZIALA!!!!");
        return new PlayerResponseImpl();
    }
}
