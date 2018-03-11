package org.grizz.game.cucumber.steps;

import cucumber.api.java.en.Given;
import org.grizz.game.cucumber.CucumberTest;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.items.Item;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LocationSteps extends CucumberTest {

    @Given("^location with id \"(.+)\" has (\\d+) items$")
    public void location_has_items_in_amount_of(String locationId, int amountOfItems) {
        List<Item> mobileItems = fromDB().location(locationId).getMobileItems();
        assertThat(mobileItems, hasSize(amountOfItems));
    }

    @Given("^location with id \"(.+)\" has \"(.+)\"$")
    public void location_has_item(String locationId, String itemName) {
        List<Item> mobileItems = fromDB().location(locationId).getMobileItems();
        assertThat(mobileItems, hasItem(item(itemName)));
    }

    @Given("^current location has \"(.+)\"$")
    public void current_location_has_item(String itemName) {
        List<Item> mobileItems = sharedData.getCurrentLocationItems().getMobileItems();
        assertThat(mobileItems, hasItem(item(itemName)));
    }

    @Given("^current location has no items$")
    public void current_location_has_no_items() {
        List<Item> mobileItems = sharedData.getCurrentLocationItems().getMobileItems();
        assertThat(mobileItems, hasSize(0));
    }

    @Given("^current location had \"(.+)\" before last command$")
    public void current_location_had_item_before_command(String itemName) {
        LocationItems locationBeforeCommand = sharedData.getCurrentLocationItemsBeforeCommand();
        LocationItems locationAfterCommand = sharedData.getCurrentLocationItems();

        assertThat(locationAfterCommand.getLocationId(), is(locationBeforeCommand.getLocationId()));

        List<Item> mobileItems = locationBeforeCommand.getMobileItems();
        assertThat(mobileItems, hasItem(item(itemName)));
    }

    @Given("^current location id is \"(.+)\"$")
    public void current_location_id_is(String expectedLocationId) {
        String locationId = sharedData.getCurrentLocationItems().getLocationId();
        assertThat(locationId, is(expectedLocationId));
    }

    @Given("^current location has (\\d+) items$")
    public void current_location_has_items_in_amount_of(int amountOfItems) {
        List<Item> mobileItems = sharedData.getCurrentLocationItems().getMobileItems();
        assertThat(mobileItems, hasSize(amountOfItems));
    }

    @Given("^current locationId has not changed$")
    public void current_location_has_not_changed() {
        String before = sharedData.getCurrentLocationItemsBeforeCommand().getLocationId();
        String after = sharedData.getCurrentLocationItems().getLocationId();
        assertThat(before, is(after));
    }
}
