package org.grizz.game.cucumber.tests.commands;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.grizz.game.cucumber.CucumberTest;
import org.grizz.game.model.Player;

import java.util.List;

import static org.grizz.game.utils.StringUtils.stripAccents;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AdminGiveItemCommandSteps extends CucumberTest {

    @Given("^player with name \"(.+)\"$")
    public void current_player(String playerName) {
        currentPlayer = playerName;
    }

    @Given("^as player with name \"(.+)\"$")
    public void as_current_player(String playerName) {
        current_player(playerName);
    }

    @Given("^there is \"(.+)\" in his backpack$")
    public void there_is_item_in_backpack(String itemName) {
        there_is_item_in_backpack(1, itemName);
    }

    @Given("^there is (\\d+) \"(.+)\" in his backpack$")
    public void there_is_item_in_backpack(int expectedAmount, String itemName) {
        int amountOfGivenItemsInBackpack = (int) fromDB().player(currentPlayer).getEquipment().getBackpack().stream()
                .map(item -> stripAccents(item.getName()))
                .filter(_itemName -> _itemName.equals(stripAccents(itemName)))
                .count();
        assertThat(amountOfGivenItemsInBackpack, is(expectedAmount));
    }

    @Given("^he has empty backpack$")
    public void having_with_empty_backpack() {
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

    @Then("^game responded with following events:$")
    public void game_responded_with_events(List<String> events) {
        events.forEach(event -> game_responded_with_event(event.replaceAll("\"", "")));
    }

    @Then("^game has not notified anyone else$")
    public void game_has_not_notified_anyone() {
        verifyZeroInteractions(notifier);
    }

    @Then("^game has notified \"(.+)\" with event \"(.+)\"$")
    public void game_has_notified(String playerName, String event) {
        verify(notifier).notify(eq(playerName), argThat(hasEvent(event)));
    }

    @Then("^game has notified \"(.+)\" with events:$")
    public void game_has_notified(String playerName, List<String> events) {
        events.forEach(event -> game_has_notified(playerName, event.replaceAll("\"", "")));
    }
}
