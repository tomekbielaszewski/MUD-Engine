package org.grizz.game.model.db.mappers;

import org.grizz.game.model.db.entities.PlayerParamEntity;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerParamsEntityMapper implements RowMapper<PlayerParamEntity> {
    @Override
    public PlayerParamEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return PlayerParamEntity.builder()
                .playerName(rs.getString("player_name"))
                .key(rs.getString("key"))
                .value(rs.getString("value"))
                .build();
    }
}
