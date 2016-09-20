package org.grizz.game.model.items;

import lombok.Data;
import lombok.experimental.Builder;

import java.util.List;

@Data
@Builder
public class Static implements Item {
    private final ItemType itemType = ItemType.STATIC;

    private String id;
    private String name;
    private String description;
    private String pickUpMessage;
    private List<CommandScript> commands;
}
