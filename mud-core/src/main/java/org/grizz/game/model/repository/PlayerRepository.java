package org.grizz.game.model.repository;

import org.grizz.game.model.Player;

public interface PlayerRepository {
    Player findByName(String name);
}
