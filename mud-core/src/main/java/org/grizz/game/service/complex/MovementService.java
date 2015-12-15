package org.grizz.game.service.complex;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.enums.Direction;

/**
 * Created by Grizz on 2015-04-26.
 */
public interface MovementService {
    void move(final Direction dir, final PlayerContext playerContext, final PlayerResponse response);

    void showCurrentLocation(final PlayerContext playerContext, final PlayerResponse response);
}
