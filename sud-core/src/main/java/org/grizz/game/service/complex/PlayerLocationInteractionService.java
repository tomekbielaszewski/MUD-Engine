package org.grizz.game.service.complex;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;

/**
 * Created by Grizz on 2015-08-22.
 */
public interface PlayerLocationInteractionService {
    public void pickUpItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);

    public void dropItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);
}
