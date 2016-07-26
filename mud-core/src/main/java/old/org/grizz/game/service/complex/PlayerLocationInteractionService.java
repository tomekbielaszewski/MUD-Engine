package old.org.grizz.game.service.complex;

import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;

/**
 * Created by Grizz on 2015-08-22.
 */
public interface PlayerLocationInteractionService {
    public void pickUpItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);

    public void dropItems(String itemName, Integer amount, PlayerContext playerContext, PlayerResponse response);

    void executeStaticItemCommand(String command, PlayerContext player, PlayerResponse response);

    boolean canExecuteItemCommand(String command, PlayerContext player);
}
