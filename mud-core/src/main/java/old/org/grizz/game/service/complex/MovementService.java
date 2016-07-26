package old.org.grizz.game.service.complex;

import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.model.enums.Direction;

/**
 * Created by Grizz on 2015-04-26.
 */
public interface MovementService {
    void moveRunningScripts(final Direction dir, final PlayerContext playerContext, final PlayerResponse response);

    void teleport(final String targetLocationId, final PlayerContext _context, final PlayerResponse _response);

    void showCurrentLocation(final PlayerContext playerContext, final PlayerResponse response);
}
