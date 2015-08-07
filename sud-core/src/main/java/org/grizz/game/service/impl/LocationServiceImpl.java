package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemStack;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public List<String> getLocationExits(PlayerContext context) {
        String currentLocationID = context.getCurrentLocation();
        Location location = locationRepo.get(currentLocationID);
        List<String> possibleExits = Lists.newArrayList();

        if (!StringUtils.isEmpty(location.getNorth())) possibleExits.add("north");
        if (!StringUtils.isEmpty(location.getSouth())) possibleExits.add("south");
        if (!StringUtils.isEmpty(location.getEast())) possibleExits.add("east");
        if (!StringUtils.isEmpty(location.getWest())) possibleExits.add("west");
        if (!StringUtils.isEmpty(location.getUp())) possibleExits.add("up");
        if (!StringUtils.isEmpty(location.getDown())) possibleExits.add("down");

        return possibleExits;
    }

    @Override
    public List<Item> getLocationItems(PlayerContext context) {
        String currentLocationID = context.getCurrentLocation();
        Location location = locationRepo.get(currentLocationID);
        List<Item> locationItems = Lists.newArrayList();

        for (ItemStack item : location.getItems()) {
            for (int i = 0; i < item.getQuantity(); i++) {
                //TODO: pomyslec nad agregacją itemków. Bo teraz bedzie troche przejebane wchodząc na lokacje ze 100 srebrnymi monetami...
                locationItems.add(itemRepo.get(item.getItemId()));
            }
        }

        return locationItems;
    }
}
