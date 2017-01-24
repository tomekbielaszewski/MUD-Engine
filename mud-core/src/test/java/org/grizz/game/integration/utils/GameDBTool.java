package org.grizz.game.integration.utils;

import org.grizz.game.model.LocationItems;
import org.grizz.game.model.Player;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class GameDBTool {
    private final MongoOperations db;

    public GameDBTool(MongoOperations db) {
        this.db = db;
    }

    public Player player(String name) {
        Query searchPlayerQuery = new Query(Criteria.where("name").is(name));
        return db.findOne(searchPlayerQuery, Player.class);
    }

    public LocationItems locationOf(String name) {
        String playerLocation = player(name).getCurrentLocation();
        Query query = new Query(Criteria.where("locationId").is(playerLocation));
        return db.findOne(query, LocationItems.class);
    }

    public LocationItems location(String locationId) {
        Query query = new Query(Criteria.where("locationId").is(locationId));
        return db.findOne(query, LocationItems.class);
    }
}
