package org.grizz.game.model.items;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Static implements Item {
    private final ItemType itemType = ItemType.STATIC;

    private String id;
    private String name;
    private String description;
    private String pickUpMessage;
    private List<ScriptCommandDto> commands;

    private String onDropScript;
    private String beforeDropScript;
    private String onReceiveScript;
    private String beforeReceiveScript;
}
