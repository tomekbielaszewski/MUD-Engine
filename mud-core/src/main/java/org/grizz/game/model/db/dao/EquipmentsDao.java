package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.EquipmentEntity;
import org.grizz.game.model.db.mappers.EquipmentEntityMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

public interface EquipmentsDao {
    @UseRowMapper(EquipmentEntityMapper.class)
    @SqlQuery("SELECT * FROM equipments " +
            "WHERE player_name = :name;")
    EquipmentEntity getByName(@Bind String name);

    @SqlUpdate("INSERT INTO equipments " +
            "VALUES (:e.playerName, :e.head, :e.torso, :e.hands, :e.legs, :e.feet, :e.meleeWeapon, :e.rangedWeapon) " +
            "ON CONFLICT (player_name) DO UPDATE SET head          = excluded.head, " +
            "                                        torso         = excluded.torso, " +
            "                                        hands         = excluded.hands, " +
            "                                        legs          = excluded.legs, " +
            "                                        feet          = excluded.feet, " +
            "                                        melee_weapon  = excluded.melee_weapon, " +
            "                                        ranged_weapon = excluded.ranged_weapon;")
    void upsert(@BindBean("e") EquipmentEntity equipmentEntity);
}
