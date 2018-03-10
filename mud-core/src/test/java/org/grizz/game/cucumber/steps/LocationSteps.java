package org.grizz.game.cucumber.steps;

import cucumber.api.java.en.Given;
import org.grizz.game.cucumber.CucumberTest;
import org.grizz.game.model.items.Item;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LocationSteps extends CucumberTest {

    @Given("^current location has \"(.+)\"$")
    public void current_location_has_item(String itemName) {
        List<Item> mobileItems = fromDB().locationOf(sharedData.getCurrentPlayer()).getMobileItems();
        assertThat(mobileItems, hasItem(item(itemName)));
    }

    @Given("^current location not changed$")
    public void current_location_has_not_changed() {
        String before = sharedData.getLocationBeforeCommand();
        String after = sharedData.getLocationAfterCommand();
        assertThat(before, is(after));
    }
}
