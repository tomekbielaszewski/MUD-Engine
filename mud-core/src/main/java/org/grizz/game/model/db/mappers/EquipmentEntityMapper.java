package org.grizz.game.model.db.mappers;

import org.grizz.game.model.db.entities.EquipmentEntity;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EquipmentEntityMapper implements RowMapper<EquipmentEntity> {
    @Override
    public EquipmentEntity map(ResultSet rs, StatementContext ctx) throws SQLException {
        return EquipmentEntity.builder()
                .playerName(rs.getString("player_name"))
                .head(rs.getString("head"))
                .torso(rs.getString("torso"))
                .hands(rs.getString("hands"))
                .legs(rs.getString("legs"))
                .feet(rs.getString("feet"))
                .meleeWeapon(rs.getString("melee_weapon"))
                .rangedWeapon(rs.getString("ranged_weapon"))
                .build();
    }
}
