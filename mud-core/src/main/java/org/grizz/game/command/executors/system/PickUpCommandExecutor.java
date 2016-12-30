package org.grizz.game.command.executors.system;

import com.google.common.collect.Lists;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.LocationService;
import org.grizz.game.service.script.ScriptBinding;
import org.grizz.game.service.script.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ScriptRepo scriptRepo;
    @Autowired
    private ScriptRunner scriptRunner;

    public void pickUp(String itemName, int amount, Player player, PlayerResponse response) {
        validateAmount(amount);

        Location location = locationRepo.get(player.getCurrentLocation());

        if (playerCanPickUpItems(itemName, amount, player, location, response)) {
            pickUpItems(itemName, amount, player, location, response);
            notifyPlayer(response);
        }
    }

    private boolean playerCanPickUpItems(String itemName, int amount, Player player, Location location, PlayerResponse response) {
        return runScript(location.getBeforePickUpScript(), itemName, amount, player, response);
    }

    private void pickUpItems(String itemName, int amount, Player player, Location location, PlayerResponse response) {
        List<Item> items = locationService.removeItems(itemName, amount, location);
        equipmentService.addItems(items, player, response);
        runScript(location.getOnPickUpScript(), itemName, amount, player, response);
    }

    private void validateAmount(int amount) {
        if (amount <= 0) throw new InvalidAmountException("cant.pickup.none.items");
    }

    private void notifyPlayer(PlayerResponse response) {
        String pickUpEvent = eventService.getEvent("event.player.picked.up.items");
        response.getPlayerEvents().add(0, pickUpEvent);
    }

    private boolean runScript(String scriptId, String itemName, int amount, Player player, PlayerResponse response) {
        boolean allowPickUpByDefault = true;
        List<ScriptBinding> scriptBindings = Lists.newArrayList(
                ScriptBinding.builder().name("itemName").object(itemName).build(),
                ScriptBinding.builder().name("amount").object(amount).build()
        );
        Optional<Boolean> optionalResponse = Optional.ofNullable(scriptId)
                .map(id -> scriptRepo.get(id))
                .map(script -> scriptRunner.execute(script, player, response, Boolean.class, scriptBindings));
        return optionalResponse.orElse(allowPickUpByDefault);
    }
}
