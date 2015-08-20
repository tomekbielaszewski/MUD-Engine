package org.grizz.game.service;

import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemStack;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
public interface LocationService {
    List<String> getLocationExits(PlayerContext context);

    List<Item> getLocationItems(PlayerContext context);

    List<Item> getLocationStaticItems(PlayerContext playerContext);

    void removeItemsFromLocation(Location location, String itemName, int amount);

    void addItemsToLocation(Location location, ItemStack itemStack);

    Location getCurrentLocation(PlayerContext playerContext);
}
