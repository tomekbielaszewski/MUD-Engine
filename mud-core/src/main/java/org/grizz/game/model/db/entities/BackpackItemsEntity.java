package org.grizz.game.model.db.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BackpackItemsEntity {
    private String playerName;
    private String itemId;
    private String itemName;
    private Integer amount;
}
