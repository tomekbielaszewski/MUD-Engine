package org.grizz.game.integration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.grizz.game.Game;
import org.grizz.game.integration.config.TestGameWithFongo;
import org.grizz.game.integration.utils.GameDBTool;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.service.notifier.Notifier;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;

@SpringApplicationConfiguration(classes = TestGameWithFongo.class)
abstract public class IntegrationTest {
    protected static final String ADMIN = "GameMaster";
    protected static final String PLAYER1 = "player1";
    protected static final String PLAYER2 = "player2";
    protected static final String PLAYER3 = "player3";
    protected static final String STARTING_LOCATION = "1";

    @Autowired
    protected Game game;
    @Autowired
    protected MongoOperations mongo;
    @Autowired
    protected Notifier notifier;

    @Before
    public void init() {
        mongo.save(createPlayer(ADMIN, STARTING_LOCATION));
        mongo.save(createPlayer(PLAYER1, STARTING_LOCATION));
        mongo.save(createPlayer(PLAYER2, STARTING_LOCATION));
        mongo.save(createPlayer(PLAYER3, STARTING_LOCATION));
    }

    @After
    public void clean() {
        mongo.dropCollection("players");
    }

    protected GameDBTool fromDB() {
        return new GameDBTool(mongo);
    }

    protected Player createPlayer(String name, String location) {
        return playerBuilder(name, location).build();
    }

    protected Player.PlayerBuilder playerBuilder(String name, String location) {
        return Player.builder()
                .name(name)
                .currentLocation(location)
                .equipment(Equipment.builder()
                        .backpack(Lists.newArrayList())
                        .build())
                .parameters(Maps.newHashMap());
    }
}
