package org.grizz.game.commands;

import org.grizz.game.model.PlayerContext;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
public interface Command {
    boolean accept(final String command);

    void execute(final String command, final PlayerContext playerContext);
}
