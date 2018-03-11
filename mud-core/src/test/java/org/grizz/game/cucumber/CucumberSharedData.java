package org.grizz.game.cucumber;

import lombok.Getter;
import lombok.Setter;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;

@Getter
@Setter
public class CucumberSharedData {
    private Player currentPlayer;
    private LocationItems currentLocationItems;
    private PlayerResponse response;

    private Player playerBeforeCommand;
    private LocationItems currentLocationItemsBeforeCommand;
}
