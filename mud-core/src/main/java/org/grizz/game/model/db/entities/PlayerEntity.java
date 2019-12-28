package org.grizz.game.model.db.entities;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;

@Value
@Builder
public class PlayerEntity {
    private String name;
    private String currentLocationId;
    private String pastLocationId;
    private Timestamp lastActivity;
}
