package org.grizz.game;

import org.grizz.game.command.engine.CommandHandler;
import org.grizz.game.exception.GameException;
import org.grizz.game.exception.GameExceptionHandler;
import org.grizz.game.exception.PlayerDoesNotExist;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Game {
    @Autowired
    private CommandHandler commandHandlerBus;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameExceptionHandler exceptionHandler;

    public PlayerResponse runCommand(String command, String playerName) {
        PlayerResponse response = new PlayerResponse();
        Player player = playerRepository.findByName(playerName);

        try {
            if (player == null) {
                throw new PlayerDoesNotExist("player.not.exist", playerName);
            }

            commandHandlerBus.execute(command, player, response);
            playerRepository.save(player);
        } catch (GameException e) {
            exceptionHandler.handle(e, response);
        }

        return response;
    }
}
