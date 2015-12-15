package org.grizz.game.service.complex;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;

/**
 * Created by Grizz on 2015-12-08.
 */
public interface PlayerEquipmentInteractionService {
    boolean canExecuteItemCommand(String command, PlayerContext player);

    void executeItemCommand(String command, PlayerContext player, PlayerResponse response);
}
