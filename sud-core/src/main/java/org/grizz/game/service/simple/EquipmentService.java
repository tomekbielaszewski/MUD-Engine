package org.grizz.game.service.simple;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-05-08.
 */
public interface EquipmentService {
    void showEquipment(PlayerContext player, PlayerResponse response);

    List<Item> getItemsInEquipment(PlayerContext context);

    void addItems(List<Item> items, PlayerContext playerContext, PlayerResponse response);

    void addItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);

    List<Item> removeItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);
}
