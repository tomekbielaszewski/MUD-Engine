package org.grizz.db.model.repository;

import org.grizz.db.model.ProcessedPlayerResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProcessedPlayerResponseRepository extends MongoRepository<ProcessedPlayerResponse, String> {
}
