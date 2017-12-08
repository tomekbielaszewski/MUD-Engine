package org.grizz.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ItemStack {
    private String name;
    private String id;
    private Integer amount;
}
