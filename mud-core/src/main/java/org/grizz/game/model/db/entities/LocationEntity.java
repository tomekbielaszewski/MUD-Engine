package org.grizz.game.model.db.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocationEntity {
    private String locationId;
    private String name;
}
