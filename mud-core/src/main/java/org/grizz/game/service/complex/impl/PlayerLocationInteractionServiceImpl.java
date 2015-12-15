package org.grizz.game.service.complex.impl;

import org.grizz.game.exception.CantMoveStaticItemException;
import org.grizz.game.exception.CantRemoveStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.CommandScript;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.complex.MultiplayerNotificationService;
import org.grizz.game.service.complex.PlayerLocationInteractionService;
import org.grizz.game.service.complex.ScriptRunnerService;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.service.simple.EventService;
import org.grizz.game.service.simple.LocationService;
import org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Grizz on 2015-08-22.
 */
@Component
public class PlayerLocationInteractionServiceImpl implements PlayerLocationInteractionService {
    @Autowired
    private CommandUtils commandUtils;

    @Autowired
    private LocationService locationService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private MultiplayerNotificationService notificationService;

    @Autowired
    private ScriptRunnerService scriptRunner;

    @Autowired
    private EventService eventService;

    @Autowired
    private ItemRepo itemRepo;

    @Override
    public void pickUpItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        if (amount == 0) {
            throw new InvalidAmountException("cant.pickup.none.items");
        }
        Location currentLocation = locationService.getCurrentLocation(playerContext);
        try {
            List<Item> itemFromLocation = locationService.removeItems(currentLocation, itemName, amount);
            equipmentService.addItems(itemFromLocation, playerContext, response);

            String pickupEvent = eventService.getEvent("multiplayer.event.player.picked.up.items", playerContext.getName(), amount.toString(), itemName);
            notificationService.broadcast(currentLocation, pickupEvent, playerContext);
        } catch (CantRemoveStaticItemException e) {
            throw new CantMoveStaticItemException("cant.pickup.static.item", e.getCause().getMessage());
        }
    }

    @Override
    public void dropItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        if (amount == 0) {
            throw new InvalidAmountException("cant.drop.none.items");
        }
        Location currentLocation = locationService.getCurrentLocation(playerContext);
        List<Item> itemsFromEquipment = equipmentService.removeItems(itemName, amount, playerContext, response);
        locationService.addItems(currentLocation, itemsFromEquipment);

        String pickupEvent = eventService.getEvent("multiplayer.event.player.drop.items", playerContext.getName(), amount.toString(), itemName);
        notificationService.broadcast(currentLocation, pickupEvent, playerContext);
    }

    @Override
    public boolean canExecuteItemCommand(String command, PlayerContext player) {
        List<Item> staticItemsOnLocation = locationService.getCurrentLocationStaticItems(player);
        return getMatchingCommandScript(command, staticItemsOnLocation) != null;
    }

    @Override
    public void executeStaticItemCommand(String command, PlayerContext player, PlayerResponse response) {
        List<Item> staticItemsOnLocation = locationService.getCurrentLocationStaticItems(player);
        CommandScript matchingCommandScript = getMatchingCommandScript(command, staticItemsOnLocation);
        if (matchingCommandScript != null) {
            scriptRunner.execute(command, matchingCommandScript.getCommand(), matchingCommandScript.getScriptId(), player, response);
        }
    }

    private CommandScript getMatchingCommandScript(String command, List<Item> items) {
        //TODO check for command duplicates on two different items. What to do then?
        CommandScript matchingCommandScript = items.stream()
                .flatMap(item -> item.getCommands().stream())
                .filter(commandScript -> commandUtils.isMatching(command, commandScript.getCommand()))
                .findAny().orElse(null);

        return matchingCommandScript;
    }
}
