package org.grizz.game.service;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;

/**
 * Created by Grizz on 2015-04-27.
 */
public interface ResponseExtractor {
    PlayerResponse extract(PlayerContext context);
}
