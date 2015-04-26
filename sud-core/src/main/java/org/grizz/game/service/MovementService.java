package org.grizz.game.service;

import org.grizz.game.model.PlayerContext;

/**
 * Created by Grizz on 2015-04-26.
 */
public interface MovementService {
    void move(Direction dir, PlayerContext playerContext);
}
