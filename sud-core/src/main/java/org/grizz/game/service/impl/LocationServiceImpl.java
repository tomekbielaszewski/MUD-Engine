package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.impl.items.ItemStackEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemStack;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Created by Grizz on 2015-04-27.
 */
@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public Location getCurrentLocation(PlayerContext playerContext) {
        String currentLocationId = playerContext.getCurrentLocation();
        return locationRepo.get(currentLocationId);
    }

    @Override
    public List<String> getLocationExits(PlayerContext context) {
        String currentLocationID = context.getCurrentLocation();
        Location location = locationRepo.get(currentLocationID);
        List<String> possibleExits = Lists.newArrayList();

        if (!StringUtils.isEmpty(location.getNorth())) possibleExits.add("north");
        if (!StringUtils.isEmpty(location.getSouth())) possibleExits.add("south");
        if (!StringUtils.isEmpty(location.getEast())) possibleExits.add("east");
        if (!StringUtils.isEmpty(location.getWest())) possibleExits.add("west");
        if (!StringUtils.isEmpty(location.getUp())) possibleExits.add("up");
        if (!StringUtils.isEmpty(location.getDown())) possibleExits.add("down");

        return possibleExits;
    }

    @Override
    public List<Item> getLocationItems(PlayerContext context) {
        String currentLocationID = context.getCurrentLocation();
        Location location = locationRepo.get(currentLocationID);
        List<Item> locationItems = Lists.newArrayList();

        for (ItemStack item : location.getItems()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                locationItems.add(itemRepo.get(item.getItemId()));
            }
        }

        return locationItems;
    }

    @Override
    public void removeItemsFromLocation(Location location, String itemName, int amount) {
        final Item item;

        try {
            item = itemRepo.getByName(itemName);
        } catch (NoSuchItemException e) {
            throw new NoSuchItemException("there.is.no.such.item.name", e);
        }

        ItemStack itemStackToRemove = location.getItems().stream()
                .filter(itemStack -> item.getId().equals(itemStack.getItemId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchItemException("no.item.on.location"));

        if (itemStackToRemove.getQuantity() < amount) {
            throw new NotEnoughItemsException("not.enough.items.on.location");
        } else {
            ItemStackEntity itemStackToRemoveEntity = (ItemStackEntity) itemStackToRemove;
            itemStackToRemoveEntity.setQuantity(itemStackToRemove.getQuantity() - amount);

            if (itemStackToRemoveEntity.getQuantity() == 0) {
                location.getItems().remove(itemStackToRemove);
            }
        }
    }

    @Override
    public void addItemToLocation(Location location, ItemStack itemStack) {
        if (itemStack.getQuantity() == 0) {
            return;
        }

        List<ItemStack> locationItems = location.getItems();
        Optional<ItemStack> optionalExistingItemStack = locationItems.stream()
                .filter(_itemStack -> _itemStack.getItemId().equals(itemStack.getItemId()))
                .findFirst();

        if (optionalExistingItemStack.isPresent()) {
            ItemStackEntity existingItemStack = (ItemStackEntity) optionalExistingItemStack.get();
            existingItemStack.setQuantity(existingItemStack.getQuantity() + itemStack.getQuantity());
        } else {
            locationItems.add(itemStack);
        }
    }
}
