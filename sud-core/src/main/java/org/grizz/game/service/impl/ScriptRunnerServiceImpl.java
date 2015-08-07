package org.grizz.game.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.MovementService;
import org.grizz.game.service.ScriptRunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
@Slf4j
@Component
public class ScriptRunnerServiceImpl implements ScriptRunnerService {
    @Autowired
    private ScriptEngine engine;
    @Autowired
    private ScriptRepo scriptRepo;
    @Autowired
    private MovementService movementService;

    @Override
    public Object execute(final String command, final String scriptId, final PlayerContext playerContext, final PlayerResponse response) {
        try {
            SimpleBindings binding = setupEngine(command, playerContext, response);
            return execute(scriptId, binding);
        } catch (ScriptException e) {
            e.printStackTrace();
        } finally {
            resetEngine();
        }

        return null;
    }

    @Override
    public Object execute(String scriptId, PlayerContext playerContext, PlayerResponse response) {
        return execute("", scriptId, playerContext, response);
    }

    private SimpleBindings setupEngine(String command, PlayerContext playerContext, PlayerResponse response) {
        SimpleBindings binding = new SimpleBindings();

        binding.put("command", command);
        binding.put("player", playerContext);
        binding.put("response", response);

        return binding;
    }

    private Object execute(String scriptId, SimpleBindings binding) throws ScriptException {
        Script script = scriptRepo.get(scriptId);
        log.info("Script [{}] with ID[{}] executed!", script.getName(), script.getId());

        return engine.eval(script.getCode(), binding);
    }

    private void resetEngine() {

    }
}
