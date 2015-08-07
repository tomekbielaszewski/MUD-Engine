package org.grizz.game.service;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.items.Item;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
public interface LocationService {
    List<String> getLocationExits(PlayerContext context);

    List<Item> getLocationItems(PlayerContext context);
}
