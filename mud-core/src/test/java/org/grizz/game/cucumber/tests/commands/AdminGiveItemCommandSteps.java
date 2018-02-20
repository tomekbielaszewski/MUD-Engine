package org.grizz.game.cucumber.tests.commands;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.grizz.game.cucumber.CucumberTest;
import org.grizz.game.model.Player;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AdminGiveItemCommandSteps extends CucumberTest {
    private static final String STARTING_LOCATION = "1";

    @Before
    public void setUp() {
        mongo.save(createPlayer(ADMIN, STARTING_LOCATION));
        mongo.save(createPlayer(PLAYER1, STARTING_LOCATION));
    }

    @Given("player with empty backpack$")
    public void player_with_empty_backpack() {
        currentPlayer = PLAYER1;
        Player player = fromDB().player(currentPlayer);
        assertThat(player, hasEmptyBackpack());
    }

    @When("^he executed following command \"(.+)\"$")
    public void player_executed_command(String command) {
        runCommand(command, currentPlayer);
    }

    @Then("^his backpack has (\\d+) items$")
    public void player_backpack_has_items_in_amount_of(int amount) {
        Player player = fromDB().player(currentPlayer);
        assertThat(player, hasThisManyItemsInBackpack(amount));
    }

    @Then("^game responded with following event \"(.+)\"$")
    public void game_responded_with_event(String event) {
        assertThat(response, hasEvent(event));
    }

    @Then("^game has not notified anyone else$")
    public void game_has_not_notified_anyone() {
        verifyZeroInteractions(notifier);
    }
}
