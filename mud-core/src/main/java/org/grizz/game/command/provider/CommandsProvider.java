package org.grizz.game.command.provider;

import org.grizz.game.command.Command;
import org.grizz.game.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommandsProvider {

    List<Command> provide(Player player);

}