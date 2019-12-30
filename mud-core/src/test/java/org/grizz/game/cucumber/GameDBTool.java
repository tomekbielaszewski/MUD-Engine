package org.grizz.game.cucumber;

import org.grizz.game.model.LocationItems;
import org.grizz.game.model.Player;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.grizz.game.model.repository.PlayerRepository;

public class GameDBTool {
    private final LocationItemsRepository locationItemsRepository;
    private final PlayerRepository playerRepository;

    public GameDBTool(LocationItemsRepository locationItemsRepository, PlayerRepository playerRepository) {
        this.locationItemsRepository = locationItemsRepository;
        this.playerRepository = playerRepository;
    }

    public Player player(String name) {
        return playerRepository.findByName(name);
    }

    public LocationItems location(String locationId) {
        return locationItemsRepository.findByLocationId(locationId);
    }

    public LocationItems locationOf(String name) {
        String playerLocation = player(name).getCurrentLocation();
        return location(playerLocation);
    }
}
