package org.grizz.game.model.repository;

import org.grizz.game.model.LocationItems;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationItemsRepository extends MongoRepository<LocationItems, String> {
    LocationItems findByLocationId(String locationId);
}
