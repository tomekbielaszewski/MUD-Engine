package old.org.grizz.game.model.items;

import old.org.grizz.game.model.enums.WeaponType;

/**
 * Created by tomasz.bielaszewski on 2015-04-29.
 */
public interface Weapon extends Item {
    WeaponType getWeaponType();

    int getMinDamage();

    int getMaxDamage();
}
