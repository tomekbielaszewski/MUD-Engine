package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.PlayerStatsEntity;
import org.grizz.game.model.db.mappers.PlayerStatsEntityMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

public interface PlayerStatsDao {
    @UseRowMapper(PlayerStatsEntityMapper.class)
    @SqlQuery("SELECT *\n" +
            "FROM player_stats\n" +
            "WHERE player_name = :name;")
    PlayerStatsEntity getByName(@Bind("name") String name);

    @SqlUpdate("INSERT INTO player_stats\n" +
            "VALUES (:p.playerName, :p.strength, :p.dexterity, :p.endurance, :p.intelligence, " +
            " :p.wisdom, :p.charisma)\n" +
            "ON CONFLICT (player_name) DO UPDATE SET strength     = excluded.strength,\n" +
            "                                        dexterity    = excluded.dexterity,\n" +
            "                                        endurance    = excluded.endurance,\n" +
            "                                        intelligence = excluded.intelligence,\n" +
            "                                        wisdom       = excluded.wisdom,\n" +
            "                                        charisma     = excluded.charisma;")
    void upsert(@BindBean("p") PlayerStatsEntity playerStatsEntity);
}
