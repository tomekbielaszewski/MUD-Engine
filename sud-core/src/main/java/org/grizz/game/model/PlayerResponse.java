package org.grizz.game.model;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
public interface PlayerResponse {
    String getLocationName();
    String getLocationDescription();

    List<String> getLocationItems();
    List<String> getPossibleExits();
    List<String> getPlayerEvents();
}
