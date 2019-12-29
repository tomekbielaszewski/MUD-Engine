package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.PlayerParamEntity;
import org.grizz.game.model.db.mappers.PlayerParamsEntityMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

public interface PlayerParamsDao {
    @UseRowMapper(PlayerParamsEntityMapper.class)
    @SqlQuery("SELECT player_name, key, value " +
            "FROM player_params " +
            "WHERE player_name = :name;")
    List<PlayerParamEntity> getByName(@Bind String name);

    @SqlUpdate("INSERT INTO player_params " +
            "VALUES (:p.playerName, :p.key, :p.value) " +
            "ON CONFLICT (player_name, key) DO UPDATE SET value = excluded.value;")
    void insert(@BindBean("p") List<PlayerParamEntity> toParamsEntity);

    @SqlUpdate("DELETE " +
            "FROM player_params " +
            "WHERE player_name = :name;")
    void removeAll(@Bind String name);
}
