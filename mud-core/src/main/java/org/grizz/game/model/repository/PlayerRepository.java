package org.grizz.game.model.repository;

import org.grizz.game.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByName(String name);

    Player findByNameIgnoreCase(String name);
    List<Player> findByLastActivityTimestampGreaterThan(long timestamp);

    List<Player> findByCurrentLocation(String id);
}
