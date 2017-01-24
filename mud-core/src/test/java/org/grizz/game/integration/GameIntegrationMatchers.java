package org.grizz.game.integration;

import org.grizz.game.model.PlayerResponse;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public interface GameIntegrationMatchers {
    default Matcher<PlayerResponse> hasEvent(String searchedEvent) {
        return new BaseMatcher<PlayerResponse>() {
            @Override
            public boolean matches(Object item) {
                final PlayerResponse response = (PlayerResponse) item;
                return hasItem(searchedEvent).matches(response.getPlayerEvents());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected player event list to contain event equal to \"" + searchedEvent + "\" but couldn't found it");
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
                description.appendText("Expected player event list to contain event like \"" + partOfSearchedEvent + "\" but couldn't found it");
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
                return hasSize(0)
                        .matches(response.getPlayerEvents());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected empty player event list but has " + amountOf + " events");
            }
        };
    }
}
