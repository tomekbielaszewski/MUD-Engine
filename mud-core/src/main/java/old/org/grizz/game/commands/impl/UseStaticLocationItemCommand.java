package old.org.grizz.game.commands.impl;

import old.org.grizz.game.commands.Command;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.complex.PlayerLocationInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
@Component
public class UseStaticLocationItemCommand implements Command {
    @Autowired
    private PlayerLocationInteractionService locationInteractionService;

    @Override
    public boolean accept(String command, PlayerContext playerContext) {
        return locationInteractionService.canExecuteItemCommand(command, playerContext);
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext, PlayerResponse response) {
        locationInteractionService.executeStaticItemCommand(command, playerContext, response);
        return response;
    }

    @Override
    public boolean accept(String command) {
        return false; //Not used - do nothing...
    }
}
