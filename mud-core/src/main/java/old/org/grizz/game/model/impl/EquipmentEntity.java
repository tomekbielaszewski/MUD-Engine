package old.org.grizz.game.model.impl;

import lombok.Data;
import lombok.experimental.Builder;
import old.org.grizz.game.model.Equipment;
import old.org.grizz.game.model.items.Armor;
import old.org.grizz.game.model.items.Item;
import old.org.grizz.game.model.items.Weapon;

import java.util.List;

/**
 * Created by Grizz on 2015-10-03.
 */
@Data
@Builder
public class EquipmentEntity implements Equipment {
    private List<Item> backpack;

    private Armor headItem;
    private Armor torsoItem;
    private Armor handsItem;
    private Armor legsItem;
    private Armor feetItem;

    private Weapon meleeWeapon;
    private Weapon rangeWeapon;
}
