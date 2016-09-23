package org.grizz.game.command.parsers;

import org.grizz.game.command.executors.UnknownCommandExecutor;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnknownCommand extends CommandParser {

    @Autowired
    private UnknownCommandExecutor unknownCommand;

    public UnknownCommand() {
        super(null);
    }

    @Override
    public boolean accept(String command) {
        return true;
    }

    @Override
    public PlayerResponse execute(String command, Player player, PlayerResponse response) {
        unknownCommand.execute(command);
        return response;
    }
}
