package org.grizz.game.service.simple;

import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.items.Item;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
public interface LocationService {
    List<String> getExits(Location location_);

    List<String> getPlayersOnLocation(Location location);

    List<Item> getCurrentLocationItems(PlayerContext context);

    List<Item> getCurrentLocationStaticItems(PlayerContext playerContext);

    List<Item> removeItems(Location location, String itemName, int amount);

    void addItems(Location location, List<Item> items);

    Location getCurrentLocation(PlayerContext playerContext);

    void saveLocationState(Location location);
}
