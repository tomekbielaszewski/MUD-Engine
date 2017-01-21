package org.grizz.game.service;

import com.google.common.collect.Lists;
import org.grizz.game.exception.CantOwnStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.ItemStack;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.converters.ItemListToCountedItemsConverter;
import org.grizz.game.model.converters.ItemListToItemStackConverter;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemType;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptBinding;
import org.grizz.game.service.script.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentService {
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private EventService eventService;
    @Autowired
    private ItemListToItemStackConverter itemStackConverter;
    @Autowired
    private ScriptRunner scriptRunner;
    @Autowired
    private ScriptRepo scriptRepo;

    public List<Item> removeItems(String itemName, int amount, Player player, PlayerResponse response) {
        if (amount <= 0) {
            throw new InvalidAmountException("cant.take.out.none.items");
        }

        Item itemTemplate = itemRepo.getByName(itemName);
        validateItemType(itemTemplate);

        List<Item> itemsFromBackpack = sameItemsInBackpack(itemTemplate, amount, player);
        if (canDrop(itemTemplate, amount, player, response)) {
            onDrop(itemTemplate, amount, player, response);
            validateAmount(itemsFromBackpack, amount);
            removeItems(itemsFromBackpack, player);
            notifyPlayer(itemName, amount, response);
        } else {
            itemsFromBackpack.clear();
        }

        return itemsFromBackpack;
    }

    public void addItems(String itemName, int amount, Player player, PlayerResponse response) {
        if (amount <= 0) {
            throw new InvalidAmountException("cant.receive.none.items");
        }
        Item item = itemRepo.getByName(itemName);
        addItems(Collections.nCopies(amount, item), player, response);
    }

    public void addItems(List<Item> items, Player player, PlayerResponse response) {
        if (items.size() == 0) {
            throw new InvalidAmountException("cant.put.in.backpack.none.items");
        }

        validateIfStaticItem(items);

        if (canReceive(items, player, response)) {
            onReceive(items, player, response);
            addItemsToBackpack(items, player);
            notifyPlayer(items, response);
        }
    }

    private void onReceive(List<Item> items, Player player, PlayerResponse response) {
        new ItemListToCountedItemsConverter().convert(items).entrySet()
                .forEach(itemCount -> onReceive(itemCount.getKey(), itemCount.getValue(), player, response));
    }

    private boolean canReceive(List<Item> items, Player player, PlayerResponse response) {
        return new ItemListToCountedItemsConverter().convert(items).entrySet().stream()
                .map(itemCount -> canReceive(itemCount.getKey(), itemCount.getValue(), player, response))
                .reduce(Boolean::logicalAnd).get();
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

    private boolean canDrop(Item item, int amount, Player player, PlayerResponse response) {
        return runScript(item.getBeforeDropScript(), item, amount, player, response);
    }

    private void onDrop(Item item, int amount, Player player, PlayerResponse response) {
        runScript(item.getOnDropScript(), item, amount, player, response);
    }

    private boolean canReceive(Item item, int amount, Player player, PlayerResponse response) {
        return runScript(item.getBeforeReceiveScript(), item, amount, player, response);
    }

    private void onReceive(Item item, int amount, Player player, PlayerResponse response) {
        runScript(item.getOnReceiveScript(), item, amount, player, response);
    }

    private boolean runScript(String scriptId, Item item, int amount, Player player, PlayerResponse response) {
        boolean allowByDefault = true;
        List<ScriptBinding> scriptBindings = Lists.newArrayList(
                ScriptBinding.builder().name("item").object(item).build(),
                ScriptBinding.builder().name("amount").object(amount).build()
        );
        Optional<Boolean> optionalResponse = Optional.ofNullable(scriptId)
                .map(id -> scriptRepo.get(id))
                .map(script -> scriptRunner.execute(script, player, response, Boolean.class, scriptBindings));
        return optionalResponse.orElse(allowByDefault);
    }
}
