package org.grizz.game.model.impl;

import lombok.Data;
import org.grizz.game.model.Location;

/**
 * Created by Grizz on 2015-04-21.
 */
@Data
public class LocationEntity implements Location {
    private String id;
    private String name;
    private String description;

    private String entryScript;
    private String leavingScript;

    private String south;
    private String north;
    private String east;
    private String west;
    private String up;
    private String down;
}
