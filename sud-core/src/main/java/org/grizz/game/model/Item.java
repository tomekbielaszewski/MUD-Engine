package org.grizz.game.model;

import org.grizz.game.model.enums.ItemType;

/**
 * Created by Grizz on 2015-04-21.
 */
public interface Item {
    String getId();

    String getName();

    ItemType getItemType();
}
