package org.grizz.game.service.simple.impl;

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
    public void addItems(Item item, Integer amount, PlayerContext player, PlayerResponse response) {
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                player.getEquipment().getBackpack().add(item);
            }
            response.getPlayerEvents().add(eventService.getEvent("player.received.items", "" + amount, item.getName()));
        }
    }

    @Override
    public List<Item> removeItems(String itemName, Integer amountToRemove, PlayerContext player, PlayerResponse response) {
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
            backpack.removeAll(itemsToRemove);
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
