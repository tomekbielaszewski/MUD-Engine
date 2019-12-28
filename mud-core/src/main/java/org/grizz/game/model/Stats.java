package org.grizz.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Stats {
    private Integer strength;
    private Integer dexterity;
    private Integer endurance;
    private Integer intelligence;
    private Integer wisdom;
    private Integer charisma;
}
