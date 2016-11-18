package org.grizz.game.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.CantMoveStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.Location;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemType;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LocationService {
    @Autowired
    private LocationItemsRepository locationItemsRepository;
    @Autowired
    private ItemRepo itemRepo;

    public List<Item> removeItems(String name, int amount, Location location) {
        validateAmount(amount, "cant.pick.up.none.items");

        Item item = itemRepo.getByName(name);
        validateIfStatic(Lists.newArrayList(item), "cant.pick.up.static.item");

        List<Item> removedItems = removeItemsFromLocation(item, amount, location);
        saveLocationItems(location);

        return removedItems;
    }

    public void addItems(String itemName, int amount, Location location) {
        validateAmount(amount, "cant.drop.none.items");
        Item item = itemRepo.getByName(itemName);
        addItems(Collections.nCopies(amount, item), location);
    }

    public void addItems(List<Item> items, Location location) {
        validateAmount(items.size(), "cant.drop.none.items");
        validateIfStatic(items, "cant.drop.static.item");

        addItemsToLocation(items, location);
        saveLocationItems(location);
    }

    private void validateAmount(int amount, String message) {
        if (amount <= 0) {
            throw new InvalidAmountException(message);
        }
    }

    private void validateIfStatic(List<Item> items, String message) {
        if (containsStaticItems(items)) {
            throw new CantMoveStaticItemException(message);
        }
    }

    private boolean containsStaticItems(List<Item> items) {
        return items.stream()
                .anyMatch(i -> i.getItemType().equals(ItemType.STATIC));
    }

    private void addItemsToLocation(List<Item> items, Location location) {
        List<Item> mobileItems = location.getItems().getMobileItems();
        mobileItems.addAll(items);
    }

    private List<Item> removeItemsFromLocation(Item itemToRemove, int amount, Location location) {
        List<Item> mobileItems = location.getItems().getMobileItems();
        List<Item> matchingItems = mobileItems.stream()
                .filter(i -> i.getName().equals(itemToRemove.getName()))
                .limit(amount)
                .collect(Collectors.toList());
        if (matchingItems.size() == 0) throw new NoSuchItemException("no.item.on.location");
        if (matchingItems.size() < amount) throw new NotEnoughItemsException("not.enough.items.on.location");

        matchingItems.forEach(mobileItems::remove);
        return matchingItems;
    }

    private void saveLocationItems(Location location) {
        locationItemsRepository.save(location.getItems());
    }
}
