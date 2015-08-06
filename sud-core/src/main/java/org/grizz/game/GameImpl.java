package org.grizz.game;

import com.google.common.collect.Lists;
import org.grizz.game.commands.CommandHandlerBus;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.ResponseExtractor;
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
    @Autowired
    private ResponseExtractor responseExtractor;

    @Override
    public PlayerResponse runCommand(String command, String player) {
        PlayerContext context = playerRepository.get(player);
        PlayerResponse response = commandHandlerBus.execute(command, context);

        //TODO CommandHandlerBuss should return response, and not extracting it from context
        PlayerResponse responseTemp = responseExtractor.extract(context);
        resetContext(context);

        return responseTemp;
    }

    private void resetContext(PlayerContext context) {
        PlayerContextImpl playerContext = (PlayerContextImpl) context;
        playerContext.setEvents(Lists.newArrayList());
    }
}
