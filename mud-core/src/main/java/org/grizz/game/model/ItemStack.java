package org.grizz.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
@AllArgsConstructor
public class ItemStack {
    private String name;
    private String id;
    private Integer amount;
}
