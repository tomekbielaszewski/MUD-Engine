package org.grizz.game.service.complex.impl;

import org.grizz.game.exception.CantMoveStaticItemException;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.items.ItemStackEntity;
import org.grizz.game.model.impl.items.StaticEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.complex.PlayerLocationInteractionService;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.service.simple.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        Location currentLocation = locationService.getCurrentLocation(playerContext);
        try {
            Item itemFromLocation = locationService.removeItemsFromLocation(currentLocation, itemName, amount);
            equipmentService.addItems(itemFromLocation, amount, playerContext, response);
        } catch (NoSuchItemException e) {
            Item item = getItem(itemName);
            if (item instanceof StaticEntity) {
                throw new CantMoveStaticItemException("cant.pickup.static.item");
            }
        }
    }

    @Override
    public void dropItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response) {
        Location currentLocation = locationService.getCurrentLocation(playerContext);
        Item itemFromEquipment = equipmentService.removeItems(itemName, amount, playerContext, response);
        locationService.addItemsToLocation(currentLocation, ItemStackEntity.builder()
                .itemId(itemFromEquipment.getId())
                .quantity(amount)
                .build());
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
