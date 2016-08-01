package org.grizz.game.command.provider;

import org.grizz.game.command.Command;
import org.grizz.game.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommandsProvider {

    List<Command> provide(Player player);// {
//        List<Command> commands = Lists.newArrayList();
//        commands.addAll(provideSystemCommands());
////        commands.addAll(provideEquipmentItemsCommands(player.getEquipment()));
////        commands.addAll(provideBackpackItemsCommands(player.getBackpack()));
////        commands.addAll(provideLocationCommands(player.getLocation()));
//        return commands;
//    }
//
//    private Collection<? extends Command> provideSystemCommands() {
//        return Lists.newArrayList();
//    }
}
