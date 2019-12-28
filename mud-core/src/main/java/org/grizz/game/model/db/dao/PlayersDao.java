package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.PlayerEntity;

import java.util.List;
import java.util.Optional;

public interface PlayersDao {
    PlayerEntity getByName(String name);

    List<String> findByLastActivityTimestampGreaterThan(long timestamp);

    List<String> findByCurrentLocation(String id);

    Optional<String> checkExistence(String name);

    void upsert(PlayerEntity playerEntity);
}
