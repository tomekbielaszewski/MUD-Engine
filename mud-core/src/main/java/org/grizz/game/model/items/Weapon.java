package org.grizz.game.model.items;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Weapon implements Item {
    private final ItemType itemType = ItemType.WEAPON;

    private String id;
    private String name;
    private String description;
    private List<ScriptCommandDto> commands;
    private WeaponType weaponType;
    private int minDamage;
    private int maxDamage;
}
