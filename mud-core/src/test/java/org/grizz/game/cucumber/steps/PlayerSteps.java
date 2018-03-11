package org.grizz.game.cucumber.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.grizz.game.cucumber.CucumberTest;
import org.grizz.game.model.Player;

import java.util.List;

import static org.grizz.game.utils.StringUtils.stripAccents;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlayerSteps extends CucumberTest {

    @Given("^player with name \"(.+)\"$")
    public void current_player(String playerName) {
        Player currentPlayer = fromDB().player(playerName);
        sharedData.setCurrentPlayer(currentPlayer);
        sharedData.setCurrentLocationItems(fromDB().location(currentPlayer.getCurrentLocation()));
    }

    @Given("^as player with name \"(.+)\"$")
    public void as_current_player(String playerName) {
        current_player(playerName);
    }

    @Given("^he has \"(.+)\" in his backpack$")
    public void he_has_item_in_backpack(String itemName) {
        he_has_items_in_backpack(1, itemName);
    }

    @Given("^he had \"(.+)\" in his backpack before last command$")
    public void he_had_item_in_backpack(String itemName) {
        he_had_items_in_backpack(1, itemName);
    }

    @Given("^he has (\\d+) \"(.+)\" in his backpack$")
    public void he_has_items_in_backpack(int expectedAmount, String itemName) {
        there_are_items_in_players_backpack(sharedData.getCurrentPlayer(), expectedAmount, itemName);
    }

    @Given("^he had (\\d+) \"(.+)\" in his backpack before last command$")
    public void he_had_items_in_backpack(int expectedAmount, String itemName) {
        there_are_items_in_players_backpack(sharedData.getPlayerBeforeCommand(), expectedAmount, itemName);
    }

    @Given("^he has empty backpack$")
    public void he_has_empty_backpack() {
        Player player = sharedData.getCurrentPlayer();
        assertThat(player, hasEmptyBackpack());
    }

    @Given("^he had empty backpack before last command$")
    public void he_had_empty_backpack() {
        Player player = sharedData.getPlayerBeforeCommand();
        assertThat(player, hasEmptyBackpack());
    }

    @Then("^he has (\\d+) items in his backpack$")
    public void player_backpack_has_items_in_amount_of(int amount) {
        Player player = sharedData.getCurrentPlayer();
        assertThat(player, hasThisManyItemsInBackpack(amount));
    }

    @Then("^he had (\\d+) items in his backpack before last command$")
    public void player_backpack_had_items_in_amount_of(int amount) {
        Player player = sharedData.getPlayerBeforeCommand();
        assertThat(player, hasThisManyItemsInBackpack(amount));
    }

    @Given("^he has parameter \"(.+)\"$")
    public void player_has_parameter(String parameter) {
        Player player = sharedData.getCurrentPlayer();
        boolean hasParameter = player.hasParameter(parameter);
        assertThat(hasParameter, is(true));
    }

    @Given("^he has no parameter \"(.+)\"$")
    public void player_has_no_parameter(String parameter) {
        Player player = sharedData.getCurrentPlayer();
        boolean hasParameter = player.hasParameter(parameter);
        assertThat(hasParameter, is(false));
    }

    @Given("^he had parameter \"(.+)\" before command$")
    public void player_had_parameter_before(String parameter) {
        Player player = sharedData.getPlayerBeforeCommand();
        boolean hasParameter = player.hasParameter(parameter);
        assertThat(hasParameter, is(true));
    }

    @Given("^he had no parameter \"(.+)\" before command$")
    public void player_had_no_parameter_before(String parameter) {
        Player player = sharedData.getPlayerBeforeCommand();
        boolean hasParameter = player.hasParameter(parameter);
        assertThat(hasParameter, is(false));
    }

    @When("^he executed following command \"(.+)\"$")
    public void player_executed_command(String command) {
        sharedData.setPlayerBeforeCommand(fromDB().player(sharedData.getCurrentPlayer().getName()));
        sharedData.setCurrentLocationItemsBeforeCommand(fromDB().locationOf(sharedData.getCurrentPlayer().getName()));

        runCommand(command, sharedData.getCurrentPlayer().getName());

        sharedData.setCurrentLocationItems(fromDB().locationOf(sharedData.getCurrentPlayer().getName()));
        sharedData.setCurrentPlayer(fromDB().player(sharedData.getCurrentPlayer().getName()));
    }

    @When("^he executed following commands$")
    public void player_executed_commands(List<String> command) {
        command.forEach(c -> player_executed_command(c.replaceAll("\"", "")));
    }

    private void there_are_items_in_players_backpack(Player player, int expectedAmount, String itemName) {
        int amountOfGivenItemsInBackpack = (int) player
                .getEquipment()
                .getBackpack()
                .stream()
                .map(item -> stripAccents(item.getName()))
                .filter(_itemName -> _itemName.equals(stripAccents(itemName)))
                .count();
        assertThat(amountOfGivenItemsInBackpack, is(expectedAmount));
    }
}
