package org.grizz.game.model.db.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerParamEntity {
    private String playerName;
    private String key;
    private String value;
}
