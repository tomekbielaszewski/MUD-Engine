package org.grizz.game.command.executors.admin;

import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemType;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.AdminRightsService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.LocationService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminPutItemCommandExecutor {
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private LocationService locationService;
    @Autowired
    private EventService eventService;
    @Autowired
    private MultiplayerNotificationService notificationService;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private AdminRightsService adminRightsService;

    public void put(String itemName, int amount, Player admin, PlayerResponse response) {
        adminRightsService.checkAdminRights(admin);
        validateAmount(amount);

        Item item = getItem(itemName);
        Location location = getLocation(admin.getCurrentLocation());

        if (isStatic(item)) {
            locationService.addStaticItem(item, location);
            notifyPlayersOnLocationAboutNewStaticItem(item, location, admin);
            notifyAdminAboutAddedStatic(item, response);
        } else {
            locationService.addItems(itemName, amount, location);
            notifyPlayersOnLocationAboutNewItems(item, amount, location, admin);
            notifyAdminAboutAddedItems(item, amount, response);
        }
    }

    private void validateAmount(int amount) {
        if (amount <= 0) throw new InvalidAmountException("admin.command.cant.put.none.items");
    }

    private boolean isStatic(Item item) {
        return ItemType.STATIC.equals(item.getItemType());
    }

    private Item getItem(String itemName) {
        return itemRepo.getByName(itemName);
    }

    private Location getLocation(String currentLocation) {
        return locationRepo.get(currentLocation);
    }

    private void notifyPlayersOnLocationAboutNewStaticItem(Item item, Location location, Player admin) {
        String event = eventService.getEvent("admin.command.static.item.add.notification.broadcast", item.getName(), admin.getName());
        notificationService.broadcast(location, event, admin);
    }

    private void notifyAdminAboutAddedStatic(Item item, PlayerResponse response) {
        String event = eventService.getEvent("admin.command.static.item.add.notification", item.getName());
        response.getPlayerEvents().add(event);
    }

    private void notifyPlayersOnLocationAboutNewItems(Item item, Integer amount, Location location, Player admin) {
        String event = eventService.getEvent("admin.command.item.put.notification.broadcast", item.getName(), amount.toString(), admin.getName());
        notificationService.broadcast(location, event, admin);
    }

    private void notifyAdminAboutAddedItems(Item item, Integer amount, PlayerResponse response) {
        String event = eventService.getEvent("admin.command.item.put.notification", item.getName(), amount.toString());
        response.getPlayerEvents().add(event);
    }
}
