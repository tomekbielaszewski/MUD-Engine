package old.org.grizz.game.model;

import old.org.grizz.game.model.items.Armor;
import old.org.grizz.game.model.items.Item;
import old.org.grizz.game.model.items.Weapon;

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