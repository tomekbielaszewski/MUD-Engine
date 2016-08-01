package org.grizz.game.command.engine;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.command.Command;
import org.grizz.game.command.provider.CommandsProvider;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommandHandler {

    @Autowired
    private CommandsProvider commandsProvider;

    @Autowired
    private Command unknownCommand;

    public PlayerResponse execute(String strCommand, Player player, PlayerResponse response) {
        List<Command> commands = commandsProvider.provide(player);
        Command command = commands.stream()
                .filter(c -> c.accept(strCommand))
                .findFirst()
                .orElse(unknownCommand);
        command.execute(strCommand, player, response);
        return response;
    }
}
