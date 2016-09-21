package org.grizz.game.service.notifier;

import org.grizz.game.model.PlayerResponse;

public interface Notifier {
    void notify(String playerName, PlayerResponse response);
}
