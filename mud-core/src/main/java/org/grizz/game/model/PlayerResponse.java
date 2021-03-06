package org.grizz.game.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class PlayerResponse {
    private List<String> playerEvents = Lists.newArrayList();
    private Location currentLocation;
    private List<String> players;
    private Equipment equipment;
}
