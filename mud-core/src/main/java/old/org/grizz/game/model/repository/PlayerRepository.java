package old.org.grizz.game.model.repository;

import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.impl.PlayerContextImpl;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
public interface PlayerRepository extends MongoRepository<PlayerContextImpl, String> {
    PlayerContext findByName(String name);
    PlayerContext findByNameIgnoreCase(String name);

    List<PlayerContext> findByCurrentLocation(String id);
}
