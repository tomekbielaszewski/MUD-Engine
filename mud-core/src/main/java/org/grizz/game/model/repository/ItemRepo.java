package org.grizz.game.model.repository;

import org.grizz.game.model.items.Item;

/**
 * Created by Grizz on 2015-04-21.
 */
public interface ItemRepo extends Repository<Item> {
    Item getByName(String itemName);
}
