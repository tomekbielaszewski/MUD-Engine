package org.grizz.game.model.repository;

import org.grizz.game.model.Player;

import java.util.List;

public interface PlayerRepository /*extends CrudRepository<Player, String>*/ {
    Player findByName(String name);

    Player findByNameIgnoreCase(String name);

    List<Player> findByLastActivityTimestampGreaterThan(long timestamp);

    List<Player> findByCurrentLocation(String id);

    void update(Player player);

    void save(Player player);
}
