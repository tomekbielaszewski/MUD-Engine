package org.grizz.game.cucumber;

import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNot.not;

public interface GameMatchers {
    default Matcher<PlayerResponse> hasEvent(String searchedEvent) {
        return new BaseMatcher<PlayerResponse>() {
            @Override
            public boolean matches(Object item) {
                final PlayerResponse response = (PlayerResponse) item;
                return hasItem(searchedEvent).matches(response.getPlayerEvents());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected player event list to contain event equal to \"" + searchedEvent + "\" but couldn't find it");
            }
        };
    }
    default Matcher<PlayerResponse> hasNoEvent(String searchedEvent) {
        return new BaseMatcher<PlayerResponse>() {
            @Override
            public boolean matches(Object item) {
                final PlayerResponse response = (PlayerResponse) item;
                return not(hasItem(searchedEvent)).matches(response.getPlayerEvents());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected player event list not to contain event equal to \"" + searchedEvent + "\" but the event was found");
            }
        };
    }

    default Matcher<PlayerResponse> hasEventLike(String partOfSearchedEvent) {
        return new BaseMatcher<PlayerResponse>() {
            @Override
            public boolean matches(Object item) {
                final PlayerResponse response = (PlayerResponse) item;
                return hasItem(containsString(partOfSearchedEvent))
                        .matches(response.getPlayerEvents());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected player event list to contain event like \"" + partOfSearchedEvent + "\" but couldn't find it");
            }
        };
    }

    default Matcher<PlayerResponse> hasNoEvents() {
        return new BaseMatcher<PlayerResponse>() {
            int amountOf;

            @Override
            public boolean matches(Object item) {
                final PlayerResponse response = (PlayerResponse) item;
                amountOf = response.getPlayerEvents().size();
                return hasSize(0).matches(response.getPlayerEvents());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected empty player event list but has " + amountOf + " events");
            }
        };
    }

    default Matcher<Player> hasEmptyBackpack() {
        return hasThisManyItemsInBackpack(0);
    }

    default Matcher<Player> hasThisManyItemsInBackpack(int expectedAmount) {
        return new BaseMatcher<Player>() {
            int backpackSize;

            @Override
            public boolean matches(Object item) {
                final Player player = (Player) item;
                backpackSize = player.getEquipment().getBackpack().size();
                return hasSize(expectedAmount).matches(player.getEquipment().getBackpack());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected player with " + expectedAmount + " items in backpack but he has " + backpackSize + " items in it");
            }
        };
    }
}
