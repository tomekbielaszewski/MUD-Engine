package org.grizz.game.model.impl.items;

import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.enums.ItemType;
import org.grizz.game.model.items.Static;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-04-29.
 */
@Data
@Builder
public class StaticEntity implements Static {
    private String id;
    private String name;
    private String description;
    private String pickUpMessage;
    private ItemType itemType;
    private List<CommandScriptEntity> commands;
}
