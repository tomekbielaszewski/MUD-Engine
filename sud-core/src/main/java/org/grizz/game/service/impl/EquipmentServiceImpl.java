package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.items.ItemStackEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemStack;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.EventService;
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
    private EventService eventService;

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
    public void dropItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        final Item item = getItem(itemName);

        Location currentLocation = locationService.getCurrentLocation(playerContext);
        this.removeItems(itemName, amount, playerContext, response);
        locationService.addItemsToLocation(currentLocation, ItemStackEntity.builder()
                .itemId(item.getId())
                .quantity(amount)
                .build());
    }

    @Override
    public void addItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        final Item item = getItem(itemName);

        ItemStack sameItemStackInEquipment = playerContext.getEquipment().stream()
                .filter(itemStack -> itemStack.getItemId().equals(item.getId()))
                .findFirst()
                .orElse(null);

        if (sameItemStackInEquipment != null) {
            ItemStackEntity sameItemStackInEquipmentEntity = (ItemStackEntity) sameItemStackInEquipment;
            sameItemStackInEquipmentEntity.setQuantity(sameItemStackInEquipment.getQuantity() + amount);
        } else {
            playerContext.getEquipment().add(
                    ItemStackEntity.builder()
                            .itemId(item.getId())
                            .quantity(amount)
                            .build());
        }

        response.getPlayerEvents().add(eventService.getEvent("player.received.items", "" + amount, itemName));
    }

    @Override
    public void removeItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        final Item item = getItem(itemName);

        ItemStack itemStackInEquipment = playerContext.getEquipment().stream()
                .filter(itemStack -> itemStack.getItemId().equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchItemException("you.have.no.such.item"));

        if (itemStackInEquipment.getQuantity() < amount) {
            throw new NotEnoughItemsException("not.enough.items.in.equipment");
        } else {
            ItemStackEntity itemStackInEquipmentEntity = (ItemStackEntity) itemStackInEquipment;
            itemStackInEquipmentEntity.setQuantity(itemStackInEquipment.getQuantity() - amount);

            if (itemStackInEquipment.getQuantity() == 0) {
                playerContext.getEquipment().remove(itemStackInEquipment);
            }
            response.getPlayerEvents().add(eventService.getEvent("player.lost.items", "" + amount, itemName));
        }
    }

    private Item getItem(String itemName) {
        final Item item;

        try {
            item = itemRepo.getByName(itemName);
        } catch (NoSuchItemException e) {
            throw new NoSuchItemException("there.is.no.such.item.name", e);
        }
        return item;
    }


}
