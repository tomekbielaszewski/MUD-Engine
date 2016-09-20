package old.org.grizz.game.model.repository;

import old.org.grizz.game.model.impl.LocationItemsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationItemsRepository extends MongoRepository<LocationItemsEntity, String> {
    LocationItemsEntity findByLocationId(String locationId);
}
