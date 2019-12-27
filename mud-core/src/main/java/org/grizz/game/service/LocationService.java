package org.grizz.game.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.*;
import org.grizz.game.model.Location;
import org.grizz.game.model.converters.ItemListToItemStackConverter;
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
        validateIfMobile(Lists.newArrayList(item), "cant.pick.up.static.item");

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
        validateIfMobile(items, "cant.drop.static.item");

        addMobileItemsToLocation(items, location);
        saveLocationItems(location);
    }

    public void addStaticItem(Item item, Location location) {
        validateIfStatic(item, "cant.add.mobile.item.as.static");
        validateStaticItemsAmountOnLocation(item, location);

        addStaticItemToLocation(item, location);
        saveLocationItems(location);
    }

    private void validateStaticItemsAmountOnLocation(Item item, Location location) {
        ItemListToItemStackConverter converter = new ItemListToItemStackConverter();
        long amountOfSameItems = converter.convert(location.getItems().getStaticItems()).stream()
                .filter(i -> item.getId().equals(i.getId()))
                .count();

        if (amountOfSameItems > 0)
            throw new CantAddStaticItemException("admin.command.cant.add.more.static.items", item.getName());
    }

    private void validateAmount(int amount, String message) {
        if (amount <= 0) {
            throw new InvalidAmountException(message);
        }
    }

    private void validateIfStatic(Item item, String message) {
        if (!isStatic(item)) {
            throw new NotStaticItemException(message);
        }
    }

    private void validateIfMobile(List<Item> items, String message) {
        if (containsStaticItems(items)) {
            throw new CantMoveStaticItemException(message);
        }
    }

    private boolean containsStaticItems(List<Item> items) {
        return items.stream()
                .anyMatch(i -> isStatic(i));
    }

    private boolean isStatic(Item i) {
        return ItemType.STATIC.equals(i.getItemType());
    }

    private void addMobileItemsToLocation(List<Item> items, Location location) {
        List<Item> mobileItems = location.getItems().getMobileItems();
        mobileItems.addAll(items);
    }

    private void addStaticItemToLocation(Item item, Location location) {
        location.getItems().getStaticItems().add(item);
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
        locationItemsRepository.update(location.getItems());
    }
}
