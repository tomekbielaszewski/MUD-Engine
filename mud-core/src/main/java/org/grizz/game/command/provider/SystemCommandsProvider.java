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
            Command lookAroundCommand,
            Command northMoveCommand,
            Command southMoveCommand,
            Command westMoveCommand,
            Command eastMoveCommand,
            Command upMoveCommand,
            Command downMoveCommand,
            Command showEquipmentCommand
    ) {
        commands.add(lookAroundCommand);
        commands.add(northMoveCommand);
        commands.add(southMoveCommand);
        commands.add(westMoveCommand);
        commands.add(eastMoveCommand);
        commands.add(upMoveCommand);
        commands.add(downMoveCommand);
        commands.add(showEquipmentCommand);
    }

    @Override
    public List<Command> provide(Player player) {
        return commands;
    }
}
