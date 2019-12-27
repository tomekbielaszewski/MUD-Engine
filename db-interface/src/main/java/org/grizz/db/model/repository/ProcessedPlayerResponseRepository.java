package org.grizz.db.model.repository;

import org.grizz.db.model.ProcessedPlayerResponse;

import java.util.List;

public interface ProcessedPlayerResponseRepository {
    void insert(List<ProcessedPlayerResponse> playerResponses);
}
