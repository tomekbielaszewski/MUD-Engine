package org.grizz.game.service.impl;

import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.ScriptRunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
@Component
public class ScriptRunnerServiceImpl implements ScriptRunnerService {
    @Autowired
    private ScriptEngine engine;
    @Autowired
    private ScriptRepo scriptRepo;

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

    private SimpleBindings setupEngine(String command, PlayerContext playerContext, PlayerResponse response) {
        SimpleBindings binding = new SimpleBindings();

        binding.put("command", command);
        binding.put("player", playerContext);
        binding.put("response", response);

        return binding;
    }

    private Object execute(String scriptId, SimpleBindings binding) throws ScriptException {
        Script script = scriptRepo.get(scriptId);
        return engine.eval(script.getCode(), binding);
    }

    private void resetEngine() {

    }
}
