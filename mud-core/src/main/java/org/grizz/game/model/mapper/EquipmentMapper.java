package org.grizz.game.model.mapper;

import org.apache.commons.lang3.StringUtils;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Weapon;
import org.grizz.game.model.repository.ItemListRepository;
import org.grizz.game.model.repository.ItemRepo;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class EquipmentMapper implements RowMapper<Equipment> {
    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private ItemListRepository itemListRepository;

    @Override
    public Equipment map(ResultSet rs, StatementContext ctx) throws SQLException {
        String head = rs.getString("head");
        String torso = rs.getString("torso");
        String hands = rs.getString("hands");
        String legs = rs.getString("legs");
        String feet = rs.getString("feet");
        String melee = rs.getString("melee");
        String range = rs.getString("range");

        String backpackId = rs.getString("backpack");
        List<Item> backpack = itemListRepository.get(backpackId);

        return Equipment.builder()
                .headItem((Armor) getItem(head))
                .torsoItem((Armor) getItem(torso))
                .handsItem((Armor) getItem(hands))
                .legsItem((Armor) getItem(legs))
                .feetItem((Armor) getItem(feet))
                .meleeWeapon((Weapon) getItem(melee))
                .rangeWeapon((Weapon) getItem(range))
                .backpack(backpack)
                .build();
    }

    private Item getItem(String id) {
        return Optional.ofNullable(id)
                .filter(StringUtils::isNotBlank)
                .map(itemRepo::get)
                .orElse(null);
    }
}
