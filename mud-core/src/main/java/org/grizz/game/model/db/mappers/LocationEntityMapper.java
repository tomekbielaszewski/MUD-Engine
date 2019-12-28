package org.grizz.game.model.db.mappers;

import org.grizz.game.model.db.entities.LocationEntity;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LocationEntityMapper implements RowMapper<LocationEntity> {
    @Override
    public LocationEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return LocationEntity.builder()
                .locationId(rs.getString("location_id"))
                .name(rs.getString("name"))
                .build();
    }
}
