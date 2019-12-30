package org.grizz.game.cucumber;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.grizz.game.Game;
import org.grizz.game.cucumber.config.TestGameConfigWithFongo;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.Stats;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.notifier.Notifier;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
        classes = TestGameConfigWithFongo.class,
        loader = SpringApplicationContextLoader.class)
@IntegrationTest
public class CucumberTest implements GameMatchers {
    protected static final String ADMIN = "GameMaster";
    protected static final String PLAYER1 = "player1";
    protected static final String PLAYER2 = "player2";
    protected static final String PLAYER3 = "player3";

    @Autowired
    protected CucumberSharedData sharedData;

    @Autowired
    protected Game game;

    @Autowired
    protected Jdbi jdbi;

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

    protected void runCommand(String command, String player) {
        sharedData.setResponse(game.runCommand(command, player));
    }

    protected GameDBTool fromDB() {
        return new GameDBTool(jdbi);
    }

    protected Player createPlayer(String name, String location) {
        return playerBuilder(name, location).build();
    }

    private Player.PlayerBuilder playerBuilder(String name, String location) {
        return Player.builder()
                .name(name)
                .currentLocation(location)
                .equipment(Equipment.builder()
                        .backpack(Lists.newArrayList())
                        .build())
                .stats(Stats.builder()
                        .build())
                .parameters(Maps.newHashMap());
    }
}
