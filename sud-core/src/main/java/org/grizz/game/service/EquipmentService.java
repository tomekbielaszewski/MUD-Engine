package org.grizz.game.service;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-05-08.
 */
public interface EquipmentService {
    List<Item> getItemsInEquipment(PlayerContext context);

    void pickUpItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);

    void dropItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);

    void addItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);

    void removeItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);
}
