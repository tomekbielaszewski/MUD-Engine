package org.grizz.game.service.complex;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;

/**
 * Created by Grizz on 2015-11-30.
 */
public interface AdministratorService {
    void teleport(String player, String targetLocationId, PlayerContext admin, PlayerResponse adminResponse);
    void give(String player, int amount, String itemName, PlayerContext admin, PlayerResponse adminResponse);
    void put(String locationId, int amount, String itemName, PlayerContext admin, PlayerResponse adminResponse);
    void put(int amount, String itemName, PlayerContext admin, PlayerResponse adminResponse);
}
