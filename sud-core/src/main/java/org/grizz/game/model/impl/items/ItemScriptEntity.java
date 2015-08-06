package org.grizz.game.model.impl.items;

import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.items.ItemScript;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
@Data
@Builder
public class ItemScriptEntity implements ItemScript {
    private String command;
    private String scriptId;
}
