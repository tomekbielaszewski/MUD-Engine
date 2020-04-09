package org.grizz.game.model.repository;

import org.grizz.game.model.Player;

import java.util.List;

public interface PlayerRepository {
    Player findByName(String name);

    Player findByNameIgnoreCase(String name);

    List<Player> findByLastActivityTimestampGreaterThan(long timestamp);

    List<Player> findByCurrentLocation(String id);

    void update(Player player);

    void insert(Player player);
}
