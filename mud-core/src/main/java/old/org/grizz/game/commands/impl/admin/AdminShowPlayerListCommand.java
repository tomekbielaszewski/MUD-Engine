package old.org.grizz.game.commands.impl.admin;

import old.org.grizz.game.commands.Command;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.complex.AdministratorService;
import old.org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Grizz on 2015-11-30.
 */
@Component
public class AdminShowPlayerListCommand implements Command {
    @Autowired
    private CommandUtils commandUtils;

    @Autowired
    private AdministratorService administratorService;

    @Override
    public boolean accept(String command) {
        return commandUtils.isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext, PlayerResponse response) {
        administratorService.showPlayerList(playerContext, response);

        return response;
    }
}
