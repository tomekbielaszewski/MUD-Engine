package org.grizz.game;

import org.grizz.game.commands.CommandHandlerBus;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-04-17.
 */
@Service
public class GameImpl implements Game {
    @Autowired
    private CommandHandlerBus commandHandlerBus;
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public PlayerResponse runCommand(String command, String player) {
        PlayerContext context = playerRepository.get(player);
        PlayerResponseImpl response = (PlayerResponseImpl) commandHandlerBus.execute(command, context);
        return response;
    }
}
