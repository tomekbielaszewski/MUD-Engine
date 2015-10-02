package org.grizz.game.model.impl.items;

import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.enums.ItemType;
import org.grizz.game.model.enums.WeaponType;
import org.grizz.game.model.items.Weapon;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-04-29.
 */
@Data
@Builder
public class WeaponEntity implements Weapon {
    private String id;
    private String name;
    private String description;
    private ItemType itemType;
    private List<CommandScriptEntity> commands;
    private WeaponType weaponType;
    private int minDamage;
    private int maxDamage;
}
