package org.grizz.game.service;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.CantDropStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemType;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Service
public class LocationService {
    @Autowired
    private LocationItemsRepository locationItemsRepository;

    public List<Item> removeItems(String name, int amount, Location location) {
        return Collections.emptyList();
    }

    public void addItems(List<Item> items, Location location) {
        if(isEmpty(items)) {
            throw new InvalidAmountException("cant.drop.none.items");
        }

        if(containsStaticItems(items)) {
            throw new CantDropStaticItemException("cant.drop.static.item");
        }

        addItemsToLocation(items, location);
        saveLocationItems(location);
    }

    private boolean containsStaticItems(List<Item> items) {
        return items.stream()
                .anyMatch(i -> i.getItemType().equals(ItemType.STATIC));
    }

    private void addItemsToLocation(List<Item> items, Location location) {
        List<Item> mobileItems = location.getItems().getMobileItems();
        mobileItems.addAll(items);
    }

    private void saveLocationItems(Location location) {
        locationItemsRepository.save(location.getItems());
    }
}
