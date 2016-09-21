package org.grizz.game.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class PlayerResponse {
    private List<String> playerEvents = Lists.newArrayList();

    public List<String> getPlayerEvents() {
        return playerEvents;
    }
}
