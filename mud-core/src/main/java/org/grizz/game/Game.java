package org.grizz.game;

import old.org.grizz.game.model.PlayerResponse;

public interface Game {
    PlayerResponse runCommand(String command, String player);
}
