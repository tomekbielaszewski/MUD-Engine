package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.PlayerStatsEntity;

public interface PlayerStatsDao {
    PlayerStatsEntity getByName(String name);

    void upsert(PlayerStatsEntity playerStatsEntity);
}
