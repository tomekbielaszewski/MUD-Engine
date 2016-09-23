package org.grizz.game.command.provider;

import com.google.common.collect.Lists;
import org.grizz.game.command.Command;
import org.grizz.game.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemCommandsProvider implements CommandsProvider {
    private List<Command> commands = Lists.newArrayList();

    @Autowired
    public void setCommands(
            Command lookAroundCommand
    ) {
        commands.add(lookAroundCommand);
    }

    @Override
    public List<Command> provide(Player player) {
        return commands;
    }
}
