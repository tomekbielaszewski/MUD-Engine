package org.grizz.game.service.complex;

import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;

/**
 * Created by Grizz on 2015-10-27.
 */
public interface MultiplayerNotificationService {
    void broadcast(Location location, String event, PlayerContext sender);

    void send(PlayerContext player, String event);
}
