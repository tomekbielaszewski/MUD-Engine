package org.grizz.game.command.executors.system;

import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.LocationService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DropCommandExecutor {
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private LocationService locationService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EventService eventService;
    @Autowired
    private MultiplayerNotificationService notificationService;

    public void drop(String itemName, int amount, Player player, PlayerResponse response) {
        validateAmount(amount);

        Location location = locationRepo.get(player.getCurrentLocation());
        dropItems(itemName, amount, player, location, response);

        notifyPlayer(itemName, amount, response);
        broadcastItemDrop(itemName, amount, location, player);
    }

    private void dropItems(String itemName, int amount, Player player, Location location, PlayerResponse response) {
        List<Item> namedItemsFromBackpack = equipmentService.removeItems(itemName, amount, player, response);
        locationService.addItems(namedItemsFromBackpack, location);
    }

    private void validateAmount(int amount) {
        if (amount <= 0) throw new InvalidAmountException("cant.drop.none.items");
    }

    private void broadcastItemDrop(String itemName, Integer amount, Location location, Player player) {
        String dropEvent = eventService.getEvent("multiplayer.event.player.drop.items", player.getName(), amount.toString(), itemName);
        notificationService.broadcast(location, dropEvent, player);
    }

    private void notifyPlayer(String itemName, Integer amount, PlayerResponse response) {
        String dropEvent = eventService.getEvent("event.player.drop.items", amount.toString(), itemName);
        response.getPlayerEvents().add(dropEvent);
    }
}
