package org.grizz.game.service.simple.impl;

import com.google.common.collect.Lists;
import org.grizz.game.exception.CantRemoveStaticItemException;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.enums.ItemType;
import org.grizz.game.model.impl.LocationItemsEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.simple.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Grizz on 2015-04-27.
 */
@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private Environment env;
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private PlayerRepository playerRepo;
    @Autowired
    private LocationItemsRepository locationItemsRepo;
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public Location getCurrentLocation(PlayerContext playerContext) {
        String currentLocationId = playerContext.getCurrentLocation();
        return locationRepo.get(currentLocationId);
    }

    @Override
    public List<String> getExits(Location location) {
        List<String> possibleExits = Lists.newArrayList();

        if (!StringUtils.isEmpty(location.getNorth())) possibleExits.add(env.getProperty("exit.north"));
        if (!StringUtils.isEmpty(location.getSouth())) possibleExits.add(env.getProperty("exit.south"));
        if (!StringUtils.isEmpty(location.getEast())) possibleExits.add(env.getProperty("exit.east"));
        if (!StringUtils.isEmpty(location.getWest())) possibleExits.add(env.getProperty("exit.west"));
        if (!StringUtils.isEmpty(location.getUp())) possibleExits.add(env.getProperty("exit.up"));
        if (!StringUtils.isEmpty(location.getDown())) possibleExits.add(env.getProperty("exit.down"));

        return possibleExits;
    }

    @Override
    public List<String> getPlayersOnLocation(Location location) {
        List<PlayerContext> players = playerRepo.findByCurrentLocation(location.getId());
        List<String> playerNicks = players.stream()
                .map(player -> player.getName())
                .collect(Collectors.toList());

        return playerNicks;
    }

    @Override
    public List<Item> getCurrentLocationItems(PlayerContext context) {
        return getCurrentLocation(context).getItems().getMobileItems();
    }

    @Override
    public List<Item> getCurrentLocationStaticItems(PlayerContext context) {
        return getCurrentLocation(context).getItems().getStaticItems();
    }

    @Override
    public List<Item> removeItems(Location location, String itemName, int amount) {
        if (amount == 0) {
            return Lists.newArrayList();
        }
        final Item item = itemRepo.getByName(itemName);

        if (ItemType.STATIC.equals(item.getItemType())) {
            throw new CantRemoveStaticItemException("cant.remove.static.item", item.getName());
        }

        List<Item> itemsOnLocation = location.getItems().getMobileItems();
        List<Item> itemsToRemove = itemsOnLocation.stream()
                .filter(locationItem -> locationItem.equals(item))
                .limit(amount)
                .collect(Collectors.toList());

        if (itemsToRemove.isEmpty()) {
            throw new NoSuchItemException("no.item.on.location");
        } else if (itemsToRemove.size() < amount) {
            throw new NotEnoughItemsException("not.enough.items.on.location");
        } else {
            for (Item itemToRemove : itemsToRemove) {
                itemsOnLocation.remove(itemToRemove);
            }

            saveLocationState(location);

            return itemsToRemove;
        }
    }

    @Override
    public void addItems(Location location, List<Item> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        List<Item> locationItems = location.getItems().getMobileItems();
        locationItems.addAll(items);
        saveLocationState(location);
    }

    @Override
    public void saveLocationState(Location location) {
        locationItemsRepo.save((LocationItemsEntity) location.getItems());
    }
}
