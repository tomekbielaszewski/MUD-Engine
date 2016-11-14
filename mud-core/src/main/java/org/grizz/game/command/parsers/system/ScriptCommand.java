package org.grizz.game.command.parsers.system;

import org.grizz.game.command.parsers.CommandParser;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.items.ScriptCommandDto;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptBinding;
import org.grizz.game.service.script.ScriptRunner;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.stream.Collectors;

public class ScriptCommand extends CommandParser {
    private final ScriptCommandDto scriptCommandDto;
    private final ScriptRepo scriptRepo;
    private final ScriptRunner scriptRunner;

    public ScriptCommand(ScriptCommandDto scriptCommandDto, ScriptRepo scriptRepo, ScriptRunner scriptRunner, Environment env) {
        super(env);
        this.scriptCommandDto = scriptCommandDto;
        this.scriptRepo = scriptRepo;
        this.scriptRunner = scriptRunner;
    }

    @Override
    public boolean accept(String command) {
        return isMatching(command, scriptCommandDto.getCommand());
    }

    @Override
    public PlayerResponse execute(String command, Player player, PlayerResponse response) {
        Script script = scriptRepo.get(scriptCommandDto.getScriptId());
        List<ScriptBinding> bindings = getCommandVariableBindings(command);
        scriptRunner.execute(script, player, response, bindings.toArray(new ScriptBinding[bindings.size()]));
        return response;
    }

    private List<ScriptBinding> getCommandVariableBindings(String command) {
        return getVariableNames(scriptCommandDto.getCommand()).stream()
                .map(v -> ScriptBinding.builder()
                        .name(v)
                        .object(getVariable(v, command, scriptCommandDto.getCommand()))
                        .build())
                .collect(Collectors.toList());
    }
}
