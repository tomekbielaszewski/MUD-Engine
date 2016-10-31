package org.grizz.db.model;

import lombok.Value;
import lombok.experimental.Builder;
import org.grizz.game.model.PlayerResponse;

@Value
@Builder
public class RawPlayerResponse {
    private String playerName;
    private PlayerResponse response;
}
