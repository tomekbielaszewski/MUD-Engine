package org.grizz.game.cucumber;

import org.grizz.game.model.LocationItems;
import org.grizz.game.model.Player;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.grizz.game.model.repository.PlayerRepository;
import org.jdbi.v3.core.Jdbi;

public class GameDBTool {
    private final Jdbi db;

    public GameDBTool(Jdbi db) {
        this.db = db;
    }

    public Player player(String name) {
        PlayerRepository playerRepository = db.onDemand(PlayerRepository.class);
        return playerRepository.findByName(name);
    }

    public LocationItems location(String locationId) {
        LocationItemsRepository locationItemsRepository = db.onDemand(LocationItemsRepository.class);
        return locationItemsRepository.findByLocationId(locationId);
    }

    public LocationItems locationOf(String name) {
        String playerLocation = player(name).getCurrentLocation();
        return location(playerLocation);
    }
}
