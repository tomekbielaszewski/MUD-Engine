package org.grizz.game.commands;

import com.google.common.collect.Lists;
import org.grizz.game.model.PlayerContext;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
public class CommandHandlerBus {
    private List<Command> commands = Lists.newArrayList();

    public void execute(String strCommand, PlayerContext context) {
        for (Command command : commands) {
            if (command.accept(strCommand)) {
                command.execute(strCommand, context);
                return;
            }
        }
    }

    public void addCommand(Command command) {
        commands.add(command);
    }
}
