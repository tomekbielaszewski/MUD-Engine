package org.grizz.game.command.provider;

import org.grizz.game.command.Command;
import org.grizz.game.model.Player;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ConversationCommandsProvider implements CommandsProvider {
    @Override
    public List<Command> provide(Player player) {
        return Collections.emptyList();
    }
}
