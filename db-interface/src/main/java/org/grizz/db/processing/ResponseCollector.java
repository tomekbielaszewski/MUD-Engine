package org.grizz.db.processing;

import com.google.common.collect.Lists;
import org.grizz.db.model.RawPlayerResponse;
import org.grizz.game.model.PlayerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseCollector {
    private List<RawPlayerResponse> rawResponses = Lists.newArrayList();

    public void collect(String playerName, PlayerResponse response) {
        RawPlayerResponse rawPlayerResponse = RawPlayerResponse.builder()
                .playerName(playerName)
                .response(response)
                .build();
        rawResponses.add(rawPlayerResponse);
    }

    public List<RawPlayerResponse> getRawResponses() {
        return rawResponses;
    }

    public void reset() {
        rawResponses.clear();
    }
}
