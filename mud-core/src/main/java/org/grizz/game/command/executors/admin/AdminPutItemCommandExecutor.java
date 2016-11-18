package org.grizz.game.command.executors.admin;

import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminPutItemCommandExecutor {
    @Autowired
    private LocationRepo locationRepo;

    public void put(String itemName, int amount, Player admin, PlayerResponse response) {

    }
}
