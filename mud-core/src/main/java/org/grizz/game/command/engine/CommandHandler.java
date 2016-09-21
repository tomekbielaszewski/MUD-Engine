package org.grizz.game.command.engine;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.command.Command;
import org.grizz.game.command.provider.CommandsProvider;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommandHandler {

    private Set<CommandsProvider> commandsProviders;

    @Autowired
    private Command unknownCommand;

    public PlayerResponse execute(String strCommand, Player player, PlayerResponse response) {
        log.debug("Collecting commands for {}", player.getName());
        List<Command> commands = commandsProviders.stream()
                .flatMap(provider -> provider.provide(player).stream())
                .collect(Collectors.toList());

        log.debug("Collected {} commands for {}", commands.size(), player.getName());
        Command command = commands.stream()
                .filter(c -> c.accept(strCommand))
                .findFirst()
                .orElse(unknownCommand);

        log.debug("{} is executing command [{}] in {}", player.getName(), strCommand, command.getClass().getCanonicalName());
        command.execute(strCommand, player, response);

        log.debug("Returning response for {}", player.getName());
        return response;
    }

    @Autowired
    public void setCommandsProviders(Set<CommandsProvider> commandsProviders) {
        this.commandsProviders = commandsProviders;
    }
}
