package org.grizz.game;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.command.engine.CommandHandler;
import org.grizz.game.exception.GameException;
import org.grizz.game.exception.GameExceptionHandler;
import org.grizz.game.exception.GameScriptException;
import org.grizz.game.exception.PlayerDoesNotExist;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
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
        } catch (GameScriptException e) {
            log.error("GameScriptException: [{}] in file:[{}] {}x{}", e.getParams());
            exceptionHandler.handleLocalized(e, response);
        } catch (GameException e) {
            exceptionHandler.handle(e, response);
        }

        return response;
    }
}
