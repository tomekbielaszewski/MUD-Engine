package org.grizz.game.service;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
public interface ScriptRunnerService {
    void executeItemCommand(final String command, final String scriptId, final PlayerContext playerContext, final PlayerResponse response);
}
