package org.grizz.game.model.db.mappers;

import org.grizz.game.model.db.entities.PlayerEntity;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerEntityMapper implements RowMapper<PlayerEntity> {
    @Override
    public PlayerEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return PlayerEntity.builder()
                .name(rs.getString("name"))
                .currentLocationId(rs.getString("current_location_id"))
                .pastLocationId(rs.getString("past_location_id"))
                .lastActivity(rs.getTimestamp("last_activity"))
                .build();
    }
}
