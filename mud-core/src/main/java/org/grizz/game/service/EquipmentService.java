package org.grizz.game.service;

import org.grizz.game.exception.CantOwnStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.ItemStack;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.converters.ItemListToItemStackConverter;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemType;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private EventService eventService;
    @Autowired
    private ItemListToItemStackConverter itemStackConverter;

    public List<Item> removeItems(String itemName, int amount, Player player, PlayerResponse response) {
        if (amount <= 0) {
            throw new InvalidAmountException("cant.take.out.none.items");
        }

        Item itemTemplate = itemRepo.getByName(itemName);
        validateItemType(itemTemplate);

        List<Item> itemsFromBackpack = sameItemsInBackpack(itemTemplate, amount, player);
        validateAmount(itemsFromBackpack, amount);
        removeItems(itemsFromBackpack, player);
        notifyPlayer(itemName, amount, response);

        return itemsFromBackpack;
    }

    public void addItems(String itemName, int amount, Player player, PlayerResponse response) {
        if (amount <= 0) {
            throw new InvalidAmountException("cant.receive.none.items");
        }
        Item item = itemRepo.getByName(itemName);
        validateItemType(item);
        addItems(Collections.nCopies(amount, item), player, response);
    }

    public void addItems(List<Item> items, Player player, PlayerResponse response) {
        if (items.size() == 0) {
            throw new InvalidAmountException("cant.put.in.backpack.none.items");
        }

        validateIfStaticItem(items);
        addItemsToBackpack(items, player);
        notifyPlayer(items, response);
    }

    private void notifyPlayer(List<Item> items, PlayerResponse response) {
        response.getPlayerEvents().add(eventService.getEvent("event.player.received.items.header"));
        List<ItemStack> stackList = itemStackConverter.convert(items);
        stackList.forEach(itemStack -> {
            String receivedItems = eventService.getEvent("event.player.received.items.single.entry", "" + itemStack.getAmount(), itemStack.getName());
            response.getPlayerEvents().add(receivedItems);
        });
    }

    private void addItemsToBackpack(List<Item> items, Player player) {
        List<Item> backpack = player.getEquipment().getBackpack();
        backpack.addAll(items);
    }

    private void validateIfStaticItem(List<Item> items) {
        items.forEach(this::validateItemType);
    }

    private void validateItemType(Item itemTemplate) {
        if (ItemType.STATIC.equals(itemTemplate.getItemType())) {
            throw new CantOwnStaticItemException("cant.own.static.item");
        }
    }

    private List<Item> sameItemsInBackpack(Item itemTemplate, int amount, Player player) {
        return player.getEquipment().getBackpack().stream()
                .filter(ownedItem -> ownedItem.getName().equals(itemTemplate.getName()))
                .limit(amount)
                .collect(Collectors.toList());
    }

    private void validateAmount(List<Item> itemsFromBackpack, int amount) {
        if (itemsFromBackpack.isEmpty()) {
            throw new NoSuchItemException("you.have.no.such.item");
        } else if (itemsFromBackpack.size() < amount) {
            throw new NotEnoughItemsException("not.enough.items.in.equipment");
        }
    }

    private void removeItems(List<Item> itemsFromBackpack, Player player) {
        List<Item> backpack = player.getEquipment().getBackpack();
        itemsFromBackpack.forEach(backpack::remove);
    }

    private void notifyPlayer(String itemName, Integer amount, PlayerResponse response) {
        String event = eventService.getEvent("event.player.lost.items", amount.toString(), itemName);
        response.getPlayerEvents().add(event);
    }
}
