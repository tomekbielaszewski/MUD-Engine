package org.grizz.db.model.repository;

import org.grizz.db.model.PlayerCommand;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerCommandRepository extends MongoRepository<PlayerCommand, String> {
    List<PlayerCommand> findByProcessedOrderByTimestampAsc(boolean processed);
}