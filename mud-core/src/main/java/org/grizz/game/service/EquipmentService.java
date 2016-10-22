package org.grizz.game.service;

import org.grizz.game.exception.CantOwnStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemType;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private EventService eventService;

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

    public void addItems(List<Item> items, Player player, PlayerResponse response) {

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
