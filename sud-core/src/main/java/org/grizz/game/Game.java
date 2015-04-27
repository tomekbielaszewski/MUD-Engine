package org.grizz.game;

import org.grizz.game.model.PlayerResponse;

/**
 * Created by Grizz on 2015-04-17.
 */
public interface Game {
    PlayerResponse runCommand(String command, String player);
}
