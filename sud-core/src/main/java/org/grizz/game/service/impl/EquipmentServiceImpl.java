package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.items.ItemStackEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemStack;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-05-08.
 */
@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private LocationService locationService;

    @Override
    public List<Item> getItemsInEquipment(PlayerContext context) {
        List<ItemStack> equipmentAsItemStack = context.getEquipment();
        List<Item> equipmentAsItems = Lists.newArrayList();

        for (ItemStack itemStack : equipmentAsItemStack) {
            Item item = itemRepo.get(itemStack.getItemId());
            for (int i = 0; i < itemStack.getQuantity(); i++) {
                equipmentAsItems.add(item);
            }
        }

        return equipmentAsItems;
    }

    @Override
    public void pickUpItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        Location currentLocation = locationService.getCurrentLocation(playerContext);
        locationService.removeItemsFromLocation(currentLocation, itemName, amount);
        this.addItems(itemName, amount, playerContext, response);
    }

    @Override
    public void addItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        final Item item;

        try {
            item = itemRepo.getByName(itemName);
        } catch (NoSuchItemException e) {
            throw new NoSuchItemException("there.is.no.such.item.name", e);
        }

        ItemStack itemsToAdd = ItemStackEntity.builder()
                .itemId(item.getId())
                .quantity(amount)
                .build();

        ItemStack sameItemStackInEquipment = playerContext.getEquipment().stream()
                .filter(itemStack -> itemStack.getItemId().equals(itemsToAdd.getItemId()))
                .findFirst()
                .orElse(null);

        if (sameItemStackInEquipment != null) {
            ItemStackEntity sameItemStackInEquipmentEntity = (ItemStackEntity) sameItemStackInEquipment;
            sameItemStackInEquipmentEntity.setQuantity(sameItemStackInEquipment.getQuantity() + itemsToAdd.getQuantity());
        } else {
            playerContext.getEquipment().add(itemsToAdd);
        }
    }


}
