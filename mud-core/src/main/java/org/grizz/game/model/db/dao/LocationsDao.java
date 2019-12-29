package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.LocationEntity;

import java.util.Optional;

public interface LocationsDao {
    Optional<String> checkExistence(String locationId);

    void insert(LocationEntity locationEntity);
}
