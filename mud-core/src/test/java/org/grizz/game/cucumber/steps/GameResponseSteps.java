package org.grizz.game.cucumber.steps;

import cucumber.api.java.en.Then;
import org.grizz.game.cucumber.CucumberTest;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class GameResponseSteps extends CucumberTest {

    @Then("^game responded with no events$")
    public void game_responded_with_no_events() {
        assertThat(sharedData.getResponse(), hasNoEvents());
    }

    @Then("^game responded with following event \"(.+)\"$")
    public void game_responded_with_event(String event) {
        assertThat(sharedData.getResponse(), hasEvent(event));
    }

    @Then("^game did not respond with following event \"(.+)\"$")
    public void game_did_not_respond_with_event(String event) {
        assertThat(sharedData.getResponse(), not(hasEvent(event)));
    }

    @Then("^game responded with event like \"(.+)\"$")
    public void game_responded_with_event_like(String event) {
        assertThat(sharedData.getResponse(), hasEventLike(event));
    }

    @Then("^game responded with following events:$")
    public void game_responded_with_events(List<String> events) {
        events.forEach(event -> game_responded_with_event(event.replaceAll("\"", "")));
    }

    @Then("^game responded with events like:$")
    public void game_responded_with_events_like(List<String> events) {
        events.forEach(event -> game_responded_with_event_like(event.replaceAll("\"", "")));
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
