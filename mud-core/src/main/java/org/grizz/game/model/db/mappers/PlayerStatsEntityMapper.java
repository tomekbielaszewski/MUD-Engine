package org.grizz.game.model.db.mappers;

import org.grizz.game.model.db.entities.PlayerStatsEntity;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerStatsEntityMapper implements RowMapper<PlayerStatsEntity> {
    @Override
    public PlayerStatsEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return PlayerStatsEntity.builder()
                .playerName(rs.getString("player_name"))
                .strength(rs.getInt("strength"))
                .dexterity(rs.getInt("dexterity"))
                .endurance(rs.getInt("endurance"))
                .intelligence(rs.getInt("intelligence"))
                .wisdom(rs.getInt("wisdom"))
                .charisma(rs.getInt("charisma"))
                .build();
    }
}
