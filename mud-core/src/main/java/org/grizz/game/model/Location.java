package org.grizz.game.model;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class Location {
    private String id;
    private String name;
    private String description;

    private String beforeEnter;
    private String onEnter;
    private String onShow;
    private String beforeLeave;
    private String onLeave;

    private String south;
    private String north;
    private String east;
    private String west;
    private String up;
    private String down;

    private LocationItems items;
}
