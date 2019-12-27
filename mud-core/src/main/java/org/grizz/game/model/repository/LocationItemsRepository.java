package org.grizz.game.model.repository;

import org.grizz.game.model.LocationItems;
import org.grizz.game.model.mappers.LocationItemsMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

public interface LocationItemsRepository {
    @UseRowMapper(LocationItemsMapper.class)
    @SqlQuery("SELECT * FROM location_items WHERE location_id=:locationId")
    LocationItems findByLocationId(@Bind String locationId);

    @SqlUpdate("SELECT * FROM 1")
    void update(LocationItems items);

    @SqlQuery("SELECT * FROM 1")
    LocationItems save(LocationItems locationItems);
}
