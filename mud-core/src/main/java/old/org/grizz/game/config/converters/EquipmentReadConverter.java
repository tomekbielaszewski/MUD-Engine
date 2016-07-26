package old.org.grizz.game.config.converters;

import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import old.org.grizz.game.model.impl.EquipmentEntity;
import old.org.grizz.game.model.items.Armor;
import old.org.grizz.game.model.items.Item;
import old.org.grizz.game.model.items.Weapon;
import old.org.grizz.game.model.repository.ItemRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Grizz on 2015-10-12.
 */
@Service
public class EquipmentReadConverter implements Converter<DBObject, EquipmentEntity> {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public EquipmentEntity convert(DBObject source) {
        String head = (String) source.get("head");
        String torso = (String) source.get("torso");
        String hands = (String) source.get("hands");
        String legs = (String) source.get("legs");
        String feet = (String) source.get("feet");
        String melee = (String) source.get("melee");
        String range = (String) source.get("range");
        List<DBObject> backpack = (List<DBObject>) source.get("backpack");

        return EquipmentEntity.builder()
                .headItem((Armor) getItem(head))
                .torsoItem((Armor) getItem(torso))
                .handsItem((Armor) getItem(hands))
                .legsItem((Armor) getItem(legs))
                .feetItem((Armor) getItem(feet))
                .meleeWeapon((Weapon) getItem(melee))
                .rangeWeapon((Weapon) getItem(range))
                .backpack(convert(backpack))
                .build();
    }

    private List<Item> convert(List<DBObject> itemsAsDBObject) {
        List<Item> items = Lists.newArrayList();
        itemsAsDBObject.stream()
                .forEach(dbObject -> {
                    Integer amount = (Integer) dbObject.get("amount");
                    Item item = itemRepo.get((String) dbObject.get("id"));
                    for (int i = 0; i < amount; i++) {
                        items.add(item);
                    }
                });
        return items;
    }

    private Item getItem(String id) {
        return StringUtils.isEmpty(id) ? null : itemRepo.get(id);
    }
}
