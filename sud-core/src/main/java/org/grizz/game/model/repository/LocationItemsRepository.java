package org.grizz.game.model.repository;

import org.grizz.game.model.impl.LocationItemsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Grizz on 2015-10-12.
 */
public interface LocationItemsRepository extends MongoRepository<LocationItemsEntity, String> {
    LocationItemsEntity findByLocationId(String locationId);
}
