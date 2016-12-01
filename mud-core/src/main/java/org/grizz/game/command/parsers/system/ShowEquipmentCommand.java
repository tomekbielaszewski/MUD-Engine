package org.grizz.game.command.parsers.system;

import org.grizz.game.command.executors.ShowEquipmentCommandExecutor;
import org.grizz.game.command.parsers.CommandParser;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ShowEquipmentCommand extends CommandParser {
    @Autowired
    private ShowEquipmentCommandExecutor commandExecutor;

    @Autowired
    public ShowEquipmentCommand(Environment env) {
        super(env);
    }

    @Override
    public boolean accept(String command) {
        return isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, Player player, PlayerResponse response) {
        commandExecutor.showEquipment(player, response);
        return response;
    }
}
