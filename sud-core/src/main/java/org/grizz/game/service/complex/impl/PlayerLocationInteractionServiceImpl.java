package org.grizz.game.service.complex.impl;

import org.grizz.game.exception.CantMoveStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.items.StaticEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.complex.PlayerLocationInteractionService;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.service.simple.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Grizz on 2015-08-22.
 */
@Component
public class PlayerLocationInteractionServiceImpl implements PlayerLocationInteractionService {
    @Autowired
    private LocationService locationService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ItemRepo itemRepo;

    @Override
    public void pickUpItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        if (amount == 0) {
            throw new InvalidAmountException("cant.pickup.none.items");
        }
        Location currentLocation = locationService.getCurrentLocation(playerContext);
        try {
            List<Item> itemFromLocation = locationService.removeItemsFromLocation(currentLocation, itemName, amount);
            equipmentService.addItems(itemFromLocation.stream().findFirst().get(), amount, playerContext, response);
        } catch (NoSuchItemException e) {
            Item item = getItem(itemName);
            if (item instanceof StaticEntity) {
                throw new CantMoveStaticItemException("cant.pickup.static.item");
            }
            throw e;
        }
    }

    @Override
    public void dropItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        if (amount == 0) {
            throw new InvalidAmountException("cant.drop.none.items");
        }
        Location currentLocation = locationService.getCurrentLocation(playerContext);
        List<Item> itemsFromEquipment = equipmentService.removeItems(itemName, amount, playerContext, response);
        locationService.addItemsToLocation(currentLocation, itemsFromEquipment);
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
