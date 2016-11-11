package org.grizz.game.command.provider;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.command.Command;
import org.grizz.game.command.parsers.CommandParser;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptBinding;
import org.grizz.game.service.script.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

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
        List<Command> commands = locationRepo.get(player.getCurrentLocation())
                .getItems().getStaticItems().stream()
                .map(staticItem -> staticItem.getCommands())
                .flatMap(c -> c.stream())
                .map(c -> new CommandParser(environment) {
                    @Override
                    public boolean accept(String command) {
                        return isMatching(command, c.getCommand());
                    }

                    @Override
                    public PlayerResponse execute(String command, Player player, PlayerResponse response) {
                        Script script = scriptRepo.get(c.getScriptId());
                        List<ScriptBinding> bindings = getVariableNames(c.getCommand()).stream()
                                .map(v -> ScriptBinding.builder()
                                        .name(v)
                                        .object(getVariable(v, command, c.getCommand()))
                                        .build())
                                .collect(Collectors.toList());
                        scriptRunner.execute(script, player, response, bindings.toArray(new ScriptBinding[bindings.size()]));
                        return response;
                    }
                })
                .collect(Collectors.toList());

        log.info("Collected {} location commands", commands.size());

        return commands;
    }
}
