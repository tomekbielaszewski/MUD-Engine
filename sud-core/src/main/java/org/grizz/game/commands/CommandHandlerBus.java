package org.grizz.game.commands;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.PlayerContext;
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
    private Environment env;

    public void execute(String strCommand, PlayerContext context) {
        String formattedStrCommand = strCommand.trim();
        formattedStrCommand = formattedStrCommand.toLowerCase();

        for (Command command : commands) {
            if (command.accept(formattedStrCommand)) {
                log.info("{} executed command[{}]", context.getName(), formattedStrCommand);

                command.execute(formattedStrCommand, context);
                return;
            }
        }

        log.info("{} executed UNKNOWN command[{}]", context.getName(), strCommand);
        context.addEvent(env.getProperty("unknown.command.invoced") + " \"" + strCommand + "\"");
    }

    public void addCommand(Command command) {
        if (!commands.add(command)) {
            throw new IllegalArgumentException("Duplicated Command! Only one instance of " + command.getClass().getCanonicalName() + " allowed");
        }
    }
}
