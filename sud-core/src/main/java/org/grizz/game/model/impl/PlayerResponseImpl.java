package org.grizz.game.model.impl;

import lombok.Data;
import org.grizz.game.model.PlayerResponse;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
@Data
public class PlayerResponseImpl implements PlayerResponse {
    private String locationName;
    private String locationDescription;
    private List<String> possibleExits;
    private List<String> playerEvents;
}
