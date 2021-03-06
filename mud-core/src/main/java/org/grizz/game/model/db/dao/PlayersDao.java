package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.PlayerEntity;
import org.grizz.game.model.db.mappers.PlayerEntityMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;
import java.util.Optional;

public interface PlayersDao {
    @UseRowMapper(PlayerEntityMapper.class)
    @SqlQuery("SELECT * " +
            "FROM players " +
            "WHERE name = :name;")
    PlayerEntity getByName(@Bind("name") String name);

    @SqlQuery("SELECT name " +
            "FROM players " +
            "WHERE last_activity > TO_TIMESTAMP(:timestamp / 1000);")
    List<String> findByLastActivityTimestampGreaterThan(@Bind("timestamp") long timestamp);

    @SqlQuery("SELECT name " +
            "FROM players " +
            "WHERE current_location_id = :id;")
    List<String> findByCurrentLocation(@Bind("id") String id);

    @SqlQuery("SELECT name " +
            "FROM players " +
            "WHERE name = :name;")
    Optional<String> checkExistence(@Bind("name") String name);

    @SqlUpdate("INSERT INTO players " +
            "VALUES (:p.name, :p.currentLocationId, COALESCE(:p.pastLocationId, :p.currentLocationId), :p.lastActivity) " +
            "ON CONFLICT (name) DO UPDATE SET name = excluded.name, " +
            "                                 current_location_id = excluded.current_location_id, " +
            "                                 past_location_id = COALESCE(excluded.past_location_id, excluded.current_location_id), " +
            "                                 last_activity = excluded.last_activity;")
    void upsert(@BindBean("p") PlayerEntity playerEntity);
}
