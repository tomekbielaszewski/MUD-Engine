package org.grizz.game.model;

import lombok.Builder;
import lombok.Data;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Weapon;

import java.util.List;

@Data
@Builder
public class Equipment {
    private List<Item> backpack;

    private Armor headItem;
    private Armor torsoItem;
    private Armor handsItem;
    private Armor legsItem;
    private Armor feetItem;

    private Weapon meleeWeapon;
    private Weapon rangeWeapon;
}
