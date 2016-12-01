package org.grizz.game.command.executors;

import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.stereotype.Component;

@Component
public class ShowEquipmentCommandExecutor {

    public void showEquipment(Player player, PlayerResponse response) {
        response.setEquipment(player.getEquipment());
    }
}
