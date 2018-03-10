package org.grizz.game.cucumber.steps;

import com.google.common.collect.Lists;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import org.grizz.game.cucumber.CucumberTest;
import org.grizz.game.model.Player;
import org.grizz.game.service.notifier.Notifier;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.grizz.game.cucumber.GameConstants.STARTING_LOCATION;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class InitializeAndCleaningSteps extends CucumberTest {

    @Autowired
    private Notifier notifier;

    @Before
    public void init() {
        mongo.dropCollection("players");
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
            if(!mongo.exists(Query.query(Criteria.where("name").is(p.getName())), Player.class))
                mongo.save(createPlayer(p.getName(), p.getCurrentLocation()));
        });
    }
}
