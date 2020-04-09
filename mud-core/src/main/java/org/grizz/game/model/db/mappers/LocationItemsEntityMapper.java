package org.grizz.game.model.db.mappers;

import org.grizz.game.model.db.entities.LocationItemsEntity;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LocationItemsEntityMapper implements RowMapper<LocationItemsEntity> {
    @Override
    public LocationItemsEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return LocationItemsEntity.builder()
                .locationId(rs.getString("location_id"))
                .itemId(rs.getString("item_id"))
                .itemName(rs.getString("item_name"))
                .amount(rs.getInt("amount"))
                .build();
    }
}
