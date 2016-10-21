package org.grizz.game.command.executors;

import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PickUpCommandExecutor {
    @Autowired
    private LocationService locationService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private EventService eventService;

    public void pickUp(String itemName, int amount, Player player, PlayerResponse response) {
        validateAmount(amount);

        Location location = locationRepo.get(player.getCurrentLocation());
        pickUpItems(itemName, amount, player, location, response);

        notifyPlayer(response);
    }

    private void pickUpItems(String itemName, int amount, Player player, Location location, PlayerResponse response) {
        List<Item> items = locationService.pickItems(itemName, amount, location);
        equipmentService.insertItems(items, player, response);
    }

    private void validateAmount(int amount) {
        if (amount <= 0) throw new InvalidAmountException("cant.pickup.none.items");
    }

    private void notifyPlayer(PlayerResponse response) {
        String pickUpEvent = eventService.getEvent("event.player.picked.up.items");
        response.getPlayerEvents().add(pickUpEvent);
    }
}
