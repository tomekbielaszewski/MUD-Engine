package org.grizz.game.model.items;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Misc implements Item {
    private final ItemType itemType = ItemType.MISC;

    private String id;
    private String name;
    private String description;
    private List<ScriptCommandDto> commands;

    private String onDropScript;
    private String beforeDropScript;
    private String onReceiveScript;
    private String beforeReceiveScript;
}
