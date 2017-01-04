package org.grizz.db.model;

import lombok.Builder;
import lombok.Value;
import org.grizz.game.model.PlayerResponse;

@Value
@Builder
public class RawPlayerResponse {
    private String playerName;
    private PlayerResponse response;
}
