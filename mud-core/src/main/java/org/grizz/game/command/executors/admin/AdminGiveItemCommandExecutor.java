package org.grizz.game.command.executors.admin;

import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.stereotype.Component;

@Component
public class AdminGiveItemCommandExecutor {
    public void give(String playerName, String itemName, int amount, Player admin, PlayerResponse response) {
    }
}
