package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.LocationItemsEntity;

import java.util.List;

public interface LocationItemsDao {
    List<LocationItemsEntity> getByLocationId(String locationId);

    void removeAll(String locationId);

    void insert(List<LocationItemsEntity> locationItemsEntities);
}
