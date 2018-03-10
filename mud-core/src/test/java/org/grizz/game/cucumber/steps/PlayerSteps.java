package org.grizz.game.cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.grizz.game.cucumber.CucumberTest;
import org.grizz.game.model.Player;

import static org.grizz.game.utils.StringUtils.stripAccents;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlayerSteps extends CucumberTest {

    @Given("^player with name \"(.+)\"$")
    public void current_player(String playerName) {
        sharedData.setCurrentPlayer(playerName);
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
        int amountOfGivenItemsInBackpack = (int) fromDB().player(sharedData.getCurrentPlayer()).getEquipment().getBackpack().stream()
                .map(item -> stripAccents(item.getName()))
                .filter(_itemName -> _itemName.equals(stripAccents(itemName)))
                .count();
        assertThat(amountOfGivenItemsInBackpack, is(expectedAmount));
    }

    @Given("^he has empty backpack$")
    public void having_with_empty_backpack() {
        Player player = fromDB().player(sharedData.getCurrentPlayer());
        assertThat(player, hasEmptyBackpack());
    }

    @Given("^he has parameter \"(.+)\"$")
    public void player_has_parameter(String parameter) {
        Player player = fromDB().player(sharedData.getCurrentPlayer());
        boolean hasParameter = player.hasParameter(parameter);
        assertThat(hasParameter, is(true));
    }

    @Given("^he has no parameter \"(.+)\"$")
    public void player_has_no_parameter(String parameter) {
        Player player = fromDB().player(sharedData.getCurrentPlayer());
        boolean hasParameter = player.hasParameter(parameter);
        assertThat(hasParameter, is(false));
    }

    @When("^he executed following command \"(.+)\"$")
    public void player_executed_command(String command) {
        runCommand(command, sharedData.getCurrentPlayer());
    }

    @Then("^his backpack has (\\d+) items$")
    public void player_backpack_has_items_in_amount_of(int amount) {
        Player player = fromDB().player(sharedData.getCurrentPlayer());
        assertThat(player, hasThisManyItemsInBackpack(amount));
    }
}
