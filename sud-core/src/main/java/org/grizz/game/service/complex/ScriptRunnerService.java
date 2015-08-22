package org.grizz.game.service.complex;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
public interface ScriptRunnerService {
    Object execute(final String command, final String scriptId, final PlayerContext playerContext, final PlayerResponse response);

    Object execute(final String scriptId, final PlayerContext playerContext, final PlayerResponse response);
}
