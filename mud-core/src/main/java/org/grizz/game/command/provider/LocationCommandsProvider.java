package org.grizz.game.command.provider;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.command.Command;
import org.grizz.game.command.parsers.system.ScriptCommand;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LocationCommandsProvider implements CommandsProvider {
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private Environment environment;
    @Autowired
    private ScriptRunner scriptRunner;
    @Autowired
    private ScriptRepo scriptRepo;

    @Override
    public List<Command> provide(Player player) {
        List<Command> commands = getCurrentLocation(player)
                .getItems().getStaticItems().stream()
                .map(Item::getCommands)
                .flatMap(Collection::stream)
                .map(c -> new ScriptCommand(c, scriptRepo, scriptRunner, environment))
                .collect(Collectors.toList());

        log.info("Collected {} location commands", commands.size());

        return commands;
    }

    private Location getCurrentLocation(Player player) {
        return locationRepo.get(player.getCurrentLocation());
    }
}
