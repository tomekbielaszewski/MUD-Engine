package org.grizz.game.model;

import org.grizz.game.model.items.Item;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
public interface PlayerResponse {
    Location getCurrentLocation();
    List<String> getPossibleExits();

    List<Item> getLocationItems();

    List<Item> getEquipmentItems();

    List<String> getPlayerEvents();
}
