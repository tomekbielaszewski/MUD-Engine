package org.grizz.game.model.db.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlayerStatsEntity {
    private String playerName;
    private Integer strength;
    private Integer dexterity;
    private Integer endurance;
    private Integer intelligence;
    private Integer wisdom;
    private Integer charisma;
}
