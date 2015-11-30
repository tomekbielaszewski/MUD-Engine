package org.grizz.game.model.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by Grizz on 2015-11-27.
 */
@Data
@Builder
@AllArgsConstructor
public class ItemStack {
    private String name;
    private String id;
    private Integer amount;
}
