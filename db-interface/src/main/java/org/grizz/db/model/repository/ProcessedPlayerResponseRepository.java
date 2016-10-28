package org.grizz.db.model.repository;

import org.grizz.db.model.ProcessedPlayerResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProcessedPlayerResponseRepository extends MongoRepository<ProcessedPlayerResponse, String> {
    List<ProcessedPlayerResponse> findBySent(boolean sent);
}
