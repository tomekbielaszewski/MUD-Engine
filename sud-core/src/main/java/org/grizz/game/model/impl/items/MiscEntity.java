package org.grizz.game.model.impl.items;

import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.enums.ItemType;
import org.grizz.game.model.items.Misc;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-04-29.
 */
@Data
@Builder
public class MiscEntity implements Misc {
    private String id;
    private String name;
    private String description;
    private ItemType itemType;
    private List<CommandScriptEntity> commands;
}
