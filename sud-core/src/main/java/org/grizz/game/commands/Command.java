package org.grizz.game.commands;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
public interface Command {
    default boolean accept(final String command, final PlayerContext playerContext) {
        return this.accept(command);
    }

    boolean accept(final String command);

    PlayerResponse execute(final String command, final PlayerContext playerContext);
}
