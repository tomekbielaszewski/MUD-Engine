package org.grizz.game.integration;

import org.grizz.game.Game;
import org.grizz.game.integration.config.TestGameWithFongo;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.notifier.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

@SpringApplicationConfiguration(classes = TestGameWithFongo.class)
public abstract class GameIntegrationHelper implements GameIntegrationMatchers {
    protected static final String ADMIN = "GameMaster";
    protected static final String PLAYER1 = "player1";
    protected static final String PLAYER2 = "player2";
    protected static final String PLAYER3 = "player3";

    protected PlayerResponse response;

    @Autowired
    protected Game game;
    @Autowired
    protected Notifier notifier;

    @Autowired
    protected ItemRepo itemRepo;

    protected void player1(String command) {
        runCommand(command, PLAYER1);
    }

    protected void player2(String command) {
        runCommand(command, PLAYER2);
    }

    protected void player3(String command) {
        runCommand(command, PLAYER3);
    }

    protected void admin(String command) {
        runCommand(command, ADMIN);
    }

    protected Item item(String name) {
        return itemRepo.getByName(name);
    }

    private void runCommand(String command, String player) {
        response = game.runCommand(command, player);
    }
}
