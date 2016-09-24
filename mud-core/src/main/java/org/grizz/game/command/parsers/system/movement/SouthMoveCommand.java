package org.grizz.game.command.parsers.system.movement;

import org.grizz.game.command.executors.system.MoveCommandExecutor;
import org.grizz.game.command.parsers.CommandParser;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.grizz.game.model.Direction.SOUTH;

@Component
public class SouthMoveCommand extends CommandParser {
    @Autowired
    private MoveCommandExecutor commandExecutor;

    @Autowired
    public SouthMoveCommand(Environment env) {
        super(env);
    }

    @Override
    public boolean accept(String command) {
        return isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, Player player, PlayerResponse response) {
        commandExecutor.move(SOUTH, player, response);
        return response;
    }
}
