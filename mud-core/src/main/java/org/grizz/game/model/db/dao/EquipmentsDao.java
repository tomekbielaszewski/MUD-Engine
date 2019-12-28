package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.EquipmentEntity;

public interface EquipmentsDao {
    EquipmentEntity getByName(String name);

    void upsert(EquipmentEntity equipmentEntity);
}
