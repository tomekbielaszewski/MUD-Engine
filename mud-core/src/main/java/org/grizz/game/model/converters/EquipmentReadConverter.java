package org.grizz.game.model.converters;

import com.mongodb.DBObject;
import org.apache.commons.lang3.StringUtils;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Weapon;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentReadConverter implements Converter<DBObject, Equipment> {
    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private DBItemPackToItemListConverter itemListConverter;

    @Override
    public Equipment convert(DBObject source) {
        String head = (String) source.get("head");
        String torso = (String) source.get("torso");
        String hands = (String) source.get("hands");
        String legs = (String) source.get("legs");
        String feet = (String) source.get("feet");
        String melee = (String) source.get("melee");
        String range = (String) source.get("range");
        List<DBObject> backpack = (List<DBObject>) source.get("backpack");

        return Equipment.builder()
                .headItem((Armor) getItem(head))
                .torsoItem((Armor) getItem(torso))
                .handsItem((Armor) getItem(hands))
                .legsItem((Armor) getItem(legs))
                .feetItem((Armor) getItem(feet))
                .meleeWeapon((Weapon) getItem(melee))
                .rangeWeapon((Weapon) getItem(range))
                .backpack(itemListConverter.convert(backpack))
                .build();
    }

    private Item getItem(String id) {
        return StringUtils.isEmpty(id) ? null : itemRepo.get(id);
    }
}
