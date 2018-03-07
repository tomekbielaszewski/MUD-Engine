package org.grizz.game.cucumber.tests;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.grizz.game.cucumber.CucumberTest;
import org.grizz.game.service.notifier.Notifier;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.grizz.game.cucumber.GameConstants.STARTING_LOCATION;

public class InitializeAndCleaningSteps extends CucumberTest {

    @Autowired
    private Notifier notifier;

    @Before
    public void init() {
        mongo.save(createPlayer(ADMIN, STARTING_LOCATION));
        mongo.save(createPlayer(PLAYER1, STARTING_LOCATION));
        mongo.save(createPlayer(PLAYER2, STARTING_LOCATION));
        mongo.save(createPlayer(PLAYER3, STARTING_LOCATION));

        Mockito.reset(notifier);
    }

    @After
    public void clean() {
        mongo.dropCollection("players");
    }
}
