package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.LocationItemsEntity;
import org.grizz.game.model.db.mappers.LocationItemsEntityMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

public interface LocationItemsDao {
    @UseRowMapper(LocationItemsEntityMapper.class)
    @SqlQuery("SELECT location_id, item_id, item_name, amount " +
            "FROM location_items " +
            "WHERE location_id = :locationId;")
    List<LocationItemsEntity> getByLocationId(@Bind("locationId") String locationId);

    @SqlUpdate("DELETE " +
            "FROM location_items " +
            "WHERE location_id = :locationId;")
    void removeAll(@Bind("locationId") String locationId);

    @SqlBatch("INSERT INTO location_items " +
            "VALUES (:l.locationId, :l.itemId, :l.itemName, :l.amount) " +
            "ON CONFLICT (location_id, item_id) DO UPDATE SET amount = excluded.amount;")
    void insert(@BindBean("l") List<LocationItemsEntity> locationItemsEntities);
}
