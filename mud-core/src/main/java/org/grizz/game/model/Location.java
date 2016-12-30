package org.grizz.game.model;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class Location {
    private String id;
    private String name;
    private String description;

    private String beforeEnterScript;
    private String onEnterScript;
    private String onShowScript;
    private String beforeLeaveScript;
    private String onLeaveScript;

    private String beforePickUpScript;
    private String onPickUpScript;

    private String south;
    private String north;
    private String east;
    private String west;
    private String up;
    private String down;

    private LocationItems items;
}
