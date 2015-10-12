package org.grizz.game.model;

import org.grizz.game.model.items.Item;

import java.util.List;

/**
 * Created by Grizz on 2015-10-12.
 */
public interface LocationItems {
    String getLocationId();

    List<Item> getMobileItems();

    List<Item> getStaticItems();
}
