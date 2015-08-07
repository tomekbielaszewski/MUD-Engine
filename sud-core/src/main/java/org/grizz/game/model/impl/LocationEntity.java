package org.grizz.game.model.impl;

import lombok.Data;
import org.grizz.game.model.Location;
import org.grizz.game.model.items.ItemStack;

import java.util.List;

/**
 * Created by Grizz on 2015-04-21.
 */
@Data
public class LocationEntity implements Location {
    private String id;
    private String name;
    private String description;

    private String beforeEnter;
    private String onEnter;
    private String onShow;
    private String beforeLeave;
    private String onLeave;

    private String south;
    private String north;
    private String east;
    private String west;
    private String up;
    private String down;

    private List<ItemStack> items;
}
