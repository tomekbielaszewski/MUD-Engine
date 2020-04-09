package org.grizz.game.model.db.mappers;

import org.grizz.game.model.db.entities.BackpackItemsEntity;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BackpackItemsEntityMapper implements RowMapper<BackpackItemsEntity> {
    @Override
    public BackpackItemsEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return BackpackItemsEntity.builder()
                .playerName(rs.getString("player_name"))
                .itemId(rs.getString("item_id"))
                .itemName(rs.getString("item_name"))
                .amount(rs.getInt("amount"))
                .build();
    }
}
