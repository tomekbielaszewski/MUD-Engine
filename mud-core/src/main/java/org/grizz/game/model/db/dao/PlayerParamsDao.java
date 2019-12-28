package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.PlayerParamEntity;

import java.util.List;

public interface PlayerParamsDao {
    List<PlayerParamEntity> getByName(String name);

    void upsert(PlayerParamEntity paramsEntity);

    void upsert(List<PlayerParamEntity> toParamsEntity);
}
