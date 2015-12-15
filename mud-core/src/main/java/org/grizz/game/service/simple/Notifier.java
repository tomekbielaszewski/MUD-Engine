package org.grizz.game.service.simple;

import org.grizz.game.model.PlayerResponse;

/**
 * Created by Grizz on 2015-10-27.
 */
public interface Notifier {
    void notify(String playerName, PlayerResponse response);
}
