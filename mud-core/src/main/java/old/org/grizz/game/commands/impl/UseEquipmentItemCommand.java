package old.org.grizz.game.commands.impl;

import old.org.grizz.game.commands.Command;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.complex.PlayerEquipmentInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
@Component
public class UseEquipmentItemCommand implements Command {
    @Autowired
    private PlayerEquipmentInteractionService playerEquipmentInteractionService;

    @Override
    public boolean accept(String command, PlayerContext playerContext) {
        return playerEquipmentInteractionService.canExecuteItemCommand(command, playerContext);
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext, PlayerResponse response) {
        playerEquipmentInteractionService.executeItemCommand(command, playerContext, response);
        return response;
    }

    @Override
    public boolean accept(String command) {
        return false; //Not used - do nothing...
    }
}
