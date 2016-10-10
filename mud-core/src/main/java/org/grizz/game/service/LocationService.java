package org.grizz.game.service;

import com.google.common.collect.Lists;
import org.grizz.game.model.Location;
import org.grizz.game.model.items.Item;

import java.util.Collections;
import java.util.List;

public class LocationService {

    public Item pickItem(String name, Location location) {
        return pickItem(name, 1, location).stream().findFirst().get();
    }

    public void dropItem(Item item, Location location) {
        dropItems(Lists.newArrayList(item), location);
    }

    public List<Item> pickItem(String name, int amount, Location location) {
        return Collections.emptyList();
    }

    public void dropItems(List<Item> items, Location location) {

    }
}
