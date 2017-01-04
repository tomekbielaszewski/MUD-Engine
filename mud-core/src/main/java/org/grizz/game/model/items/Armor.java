package org.grizz.game.model.items;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Armor implements Item {
    private final ItemType itemType = ItemType.ARMOR;

    private String id;
    private String name;
    private String description;
    private List<ScriptCommandDto> commands;
}
