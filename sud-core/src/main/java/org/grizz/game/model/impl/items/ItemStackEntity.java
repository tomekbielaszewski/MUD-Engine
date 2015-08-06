package org.grizz.game.model.impl.items;

import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.items.ItemStack;

/**
 * Created by tomasz.bielaszewski on 2015-04-30.
 */
@Data
@Builder
public class ItemStackEntity implements ItemStack {
    private int quantity;
    private String itemId;
}
