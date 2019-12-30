package org.grizz.game.cucumber.steps;

import com.google.common.collect.Lists;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import org.grizz.game.cucumber.CucumberTest;
import org.grizz.game.model.Player;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.notifier.Notifier;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.grizz.game.cucumber.GameConstants.STARTING_LOCATION;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class InitializeAndCleaningSteps extends CucumberTest {
    private final Notifier notifier;
    private final PlayerRepository playerRepository;

    @Autowired
    public InitializeAndCleaningSteps(Notifier notifier, PlayerRepository playerRepository) {
        this.notifier = notifier;
        this.playerRepository = playerRepository;
    }

    @Before
    public void init() {
        jdbi.useHandle(h -> h.execute("TRUNCATE TABLE players CASCADE"));
        Mockito.reset(notifier);
    }

    @Given("I have standard set of players on starting location")
    public void create_players_on_starting_location() {
        create_players(Lists.newArrayList(
                createPlayer(ADMIN, STARTING_LOCATION),
                createPlayer(PLAYER1, STARTING_LOCATION),
                createPlayer(PLAYER2, STARTING_LOCATION),
                createPlayer(PLAYER3, STARTING_LOCATION)
        ));
    }

    @Given("I have following players:")
    public void create_players(List<Player> players) {
        players.forEach(p -> {
            assertThat(p.getName(), not(nullValue()));
            assertThat(p.getCurrentLocation(), not(nullValue()));
            if (!playerExist(p.getName())) {
                savePlayer(p);
            }
        });
    }

    private void savePlayer(Player player) {
        playerRepository.insert(player);
    }

    private boolean playerExist(String name) {
        return jdbi.withHandle(h -> h.createQuery("SELECT count(*) FROM players WHERE name = '" + name + "'")
                .map((rs, ctx) -> rs.getInt("count"))
                .findOne()
                .filter(i -> i > 0)
                .isPresent()
        );
    }
}
