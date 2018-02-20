package org.grizz.game.cucumber.tests;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.grizz.game.cucumber.CucumberTest;

import static org.grizz.game.cucumber.GameConstants.STARTING_LOCATION;

public class InitializeAndCleaningSteps extends CucumberTest {

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
}
