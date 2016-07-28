package org.grizz.game.command.engine;

import com.google.common.collect.Lists;
import org.grizz.game.command.Command;
import org.grizz.game.model.Player;

import java.util.Collection;
import java.util.List;

public class CommandsProvider {
    public List<Command> provide(Player player) {
        List<Command> commands = Lists.newArrayList();
        commands.addAll(provideSystemCommands());
//        commands.addAll(provideEquipmentItemsCommands(player.getEquipment()));
//        commands.addAll(provideBackpackItemsCommands(player.getBackpack()));
//        commands.addAll(provideLocationCommands(player.getLocation()));
        return commands;
    }

    private Collection<? extends Command> provideSystemCommands() {
        return Lists.newArrayList();
    }
}
