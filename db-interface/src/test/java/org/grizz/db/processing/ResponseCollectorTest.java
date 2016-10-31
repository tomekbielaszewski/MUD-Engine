package org.grizz.db.processing;

import org.grizz.db.model.RawPlayerResponse;
import org.grizz.game.model.PlayerResponse;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

public class ResponseCollectorTest {
    private static final String PLAYER_NAME = "player name";

    private ResponseCollector collector = new ResponseCollector();

    @Test
    public void gettingZeroWhenNotCollectedAnything() throws Exception {
        List<RawPlayerResponse> rawResponses = collector.getRawResponses();

        assertThat(rawResponses, hasSize(0));
    }

    @Test
    public void resetsWhenNotCollectedAnything() throws Exception {
        collector.reset();
        List<RawPlayerResponse> rawResponses = collector.getRawResponses();

        assertThat(rawResponses, hasSize(0));
    }

    @Test
    public void collectsOneAndReturnsOne() throws Exception {
        PlayerResponse response = new PlayerResponse();

        collector.collect(PLAYER_NAME, response);
        List<RawPlayerResponse> rawResponses = collector.getRawResponses();

        assertThat(rawResponses, hasSize(1));
        assertThat(rawResponses.get(0).getPlayerName(), is(PLAYER_NAME));
        assertThat(rawResponses.get(0).getResponse(), is(response));
    }

    @Test
    public void collectsManyAndReturnsMany() throws Exception {
        PlayerResponse response = new PlayerResponse();

        collector.collect(PLAYER_NAME, response);
        collector.collect(PLAYER_NAME, response);
        collector.collect(PLAYER_NAME, response);
        collector.collect(PLAYER_NAME, response);
        List<RawPlayerResponse> rawResponses = collector.getRawResponses();

        assertThat(rawResponses, hasSize(4));
    }

    @Test
    public void resetRemovesAllResponses() throws Exception {
        PlayerResponse response = new PlayerResponse();

        collector.collect(PLAYER_NAME, response);
        collector.collect(PLAYER_NAME, response);
        collector.collect(PLAYER_NAME, response);
        collector.collect(PLAYER_NAME, response);
        collector.reset();
        List<RawPlayerResponse> rawResponses = collector.getRawResponses();

        assertThat(rawResponses, hasSize(0));
    }
}