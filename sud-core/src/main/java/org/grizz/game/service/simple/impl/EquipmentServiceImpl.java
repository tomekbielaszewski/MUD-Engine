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
    public void showEquipment(PlayerContext player, PlayerResponse response) {
        List<Item> itemsInEquipment = getItemsInEquipment(player);
        response.getEquipmentItems().addAll(itemsInEquipment);

        String equipmentTitle;
        if (itemsInEquipment.isEmpty()) {
            equipmentTitle = eventService.getEvent("equipment.empty");
        } else {
            equipmentTitle = eventService.getEvent("equipment.title");
        }
        response.getPlayerEvents().add(equipmentTitle);
    }

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
        //FIXME: co jesli player otrzyma liste roznych itemkow? wiadomosc zwrotna bedzie bledna
        response.getPlayerEvents().add(eventService.getEvent("player.received.items", "" + items.size(), items.stream().findFirst().get().getName()));
    }

    @Override
    public void addItems(String itemName, Integer amount, PlayerContext player, PlayerResponse response) {
        Item item = itemRepo.getByName(itemName);
        List<Item> items = Lists.newArrayList();

        for (int i = 0; i < amount; i++) {
            items.add(item);
        }

        this.addItems(items, player, response);
    }

    @Override
    public List<Item> removeItems(String itemName, Integer amountToRemove, PlayerContext player, PlayerResponse response) {
        if (amountToRemove <= 0) {
            return Lists.newArrayList();
        }
        final Item item = itemRepo.getByName(itemName);

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
}
