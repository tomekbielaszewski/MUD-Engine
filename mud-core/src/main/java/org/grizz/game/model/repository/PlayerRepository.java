package org.grizz.game.model.repository;

import org.grizz.game.model.Player;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface PlayerRepository {
    @SqlQuery("SELECT * FROM 1")
    Player findByName(String name);

    @SqlQuery("SELECT * FROM 1")
    Player findByNameIgnoreCase(String name);

    @SqlQuery("SELECT * FROM 1")
    List<Player> findByLastActivityTimestampGreaterThan(long timestamp);

    @SqlQuery("SELECT * FROM 1")
    List<Player> findByCurrentLocation(String id);

    @SqlUpdate("SELECT * FROM 1")
    void update(Player player);

    @SqlUpdate("SELECT * FROM 1")
    void insert(Player player);
}
