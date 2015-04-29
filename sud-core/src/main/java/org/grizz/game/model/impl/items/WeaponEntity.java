package org.grizz.game.model.impl.items;

import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.enums.ItemType;
import org.grizz.game.model.items.Weapon;

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
}
