package org.grizz.game.service.simple.impl;

import com.google.common.collect.Lists;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.service.simple.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tomasz.bielaszewski on 2015-05-08.
 */
@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private ItemRepo itemRepo;

    @Autowired
    private EventService eventService;

    @Override
    public List<Item> getItemsInEquipment(PlayerContext context) {
        return context.getEquipment().getBackpack();
    }

    @Override
    public void addItems(List<Item> items, PlayerContext player, PlayerResponse response) {
        if (items == null || items.isEmpty()) {
            return;
        }

        player.getEquipment().getBackpack().addAll(items);
        response.getPlayerEvents().add(eventService.getEvent("player.received.items", "" + items.size(), items.stream().findFirst().get().getName()));
    }

    @Override
    public List<Item> removeItems(String itemName, Integer amountToRemove, PlayerContext player, PlayerResponse response) {
        if (amountToRemove <= 0) {
            return Lists.newArrayList();
        }
        final Item item = getItem(itemName);

        List<Item> backpack = player.getEquipment().getBackpack();
        List<Item> itemsToRemove = backpack.stream()
                .filter(ownedItem -> ownedItem.getName().equals(item.getName()))
                .limit(amountToRemove)
                .collect(Collectors.toList());

        if (itemsToRemove.isEmpty()) {
            throw new NoSuchItemException("you.have.no.such.item");
        } else if (itemsToRemove.size() < amountToRemove) {
            throw new NotEnoughItemsException("not.enough.items.in.equipment");
        } else {
            for (Item itemToRemove : itemsToRemove) {
                backpack.remove(itemToRemove);
            }
            response.getPlayerEvents().add(eventService.getEvent("player.lost.items", "" + amountToRemove, itemName));

            return itemsToRemove;
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
