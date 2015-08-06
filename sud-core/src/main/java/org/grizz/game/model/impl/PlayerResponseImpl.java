package org.grizz.game.model.impl;

import com.google.common.collect.Lists;
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
    private List<String> locationItems = Lists.newArrayList();
    private List<String> possibleExits = Lists.newArrayList();
    private List<String> playerEvents = Lists.newArrayList();
}
