package org.grizz.game.commands;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Set;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@Slf4j
public class CommandHandlerBus {
    private Set<Command> commands = Sets.newHashSet();

    @Autowired
    private Command unknownCommand;

    @Autowired
    private Environment env;

    public PlayerResponse execute(String strCommand, PlayerContext context) {
        final String formattedStrCommand = StringUtils.stripAccents(strCommand.trim().toLowerCase())
                .replaceAll("ł", "l")
                .replaceAll("Ł", "L");

        Command commandHandler = commands.stream()
                .filter(x -> x.accept(formattedStrCommand, context))
                .findFirst()
                .orElse(unknownCommand);

        log.info("{} executed {} using '{}'", context.getName(), commandHandler.getClass().getSimpleName(), formattedStrCommand);
        return commandHandler.execute(formattedStrCommand, context);
    }

    public void addCommand(Command command) {
        if (!commands.add(command)) {
            throw new IllegalArgumentException("Duplicated Command! Only one instance of " + command.getClass().getName() + " allowed");
        }
    }
}
