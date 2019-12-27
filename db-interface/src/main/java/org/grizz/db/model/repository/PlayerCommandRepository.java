package org.grizz.db.model.repository;

import org.grizz.db.model.PlayerCommand;

import java.util.List;

public interface PlayerCommandRepository {
    List<PlayerCommand> findByProcessedOrderByTimestampAsc(boolean processed);

    void insert(PlayerCommand command);
}
