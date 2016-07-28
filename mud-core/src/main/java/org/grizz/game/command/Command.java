package org.grizz.game.command;

import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;

public interface Command {
    boolean accept(final String command);

    PlayerResponse execute(final String command, final Player player, PlayerResponse response);
}
