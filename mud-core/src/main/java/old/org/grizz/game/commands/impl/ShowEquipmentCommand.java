package old.org.grizz.game.commands.impl;

import old.org.grizz.game.commands.Command;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.simple.EquipmentService;
import old.org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tomasz.bielaszewski on 2015-04-30.
 */
@Service
public class ShowEquipmentCommand implements Command {
    @Autowired
    private CommandUtils commandUtils;
    @Autowired
    private EquipmentService equipmentService;

    @Override
    public boolean accept(String command) {
        return commandUtils.isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext, PlayerResponse response) {
        equipmentService.showEquipment(playerContext, response);
        return response;
    }
}
