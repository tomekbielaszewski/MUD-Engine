package org.grizz.game.service;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.items.Item;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-05-08.
 */
public interface EquipmentService {
    void show(PlayerContext context);

    List<Item> getItemsInEquipment(PlayerContext context);
}
