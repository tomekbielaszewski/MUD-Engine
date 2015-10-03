package org.grizz.game.model;

import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Weapon;

import java.util.List;

/**
 * Created by Grizz on 2015-10-03.
 */
public interface Equipment {
    List<Item> getBackpack();

    Armor getHeadItem();

    Armor getTorsoItem();

    Armor getHandsItem();

    Armor getLegsItem();

    Armor getFeetItem();

    Weapon getMeleeWeapon();

    Weapon getRangeWeapon();
}
