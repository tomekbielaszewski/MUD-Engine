package org.grizz.game.model.items;

import lombok.Data;
import lombok.experimental.Builder;

import java.util.List;

@Data
@Builder
public class Weapon implements Item {
    private final ItemType itemType = ItemType.WEAPON;

    private String id;
    private String name;
    private String description;
    private List<CommandScript> commands;
    private WeaponType weaponType;
    private int minDamage;
    private int maxDamage;
}
