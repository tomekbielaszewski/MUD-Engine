package org.grizz.game.service.complex.impl;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.commands.CommandHandlerBus;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.complex.PlayerLocationInteractionService;
import org.grizz.game.service.complex.ScriptRunnerService;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.service.simple.LocationService;
import org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    @Lazy
    @Autowired
    private CommandHandlerBus commandHandlerBus;
    @Lazy
    @Autowired
    private LocationRepo locationRepo;
    @Lazy
    @Autowired
    private ItemRepo itemRepo;
    @Lazy
    @Autowired
    private PlayerLocationInteractionService playerLocationInteractionService;
    @Lazy
    @Autowired
    private EquipmentService equipmentService;
    @Lazy
    @Autowired
    private LocationService locationService;
    @Lazy
    @Autowired
    private CommandUtils commandUtils;

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

        binding.put("locationRepo", locationRepo);
        binding.put("itemRepo", itemRepo);
        binding.put("playerLocationInteractionService", playerLocationInteractionService);
        binding.put("equipmentService", equipmentService);
        binding.put("locationService", locationService);
        binding.put("commandRunner", commandHandlerBus);
        binding.put("commandUtils", commandUtils);

        return binding;
    }

    private Object execute(String scriptId, SimpleBindings binding) throws ScriptException {
        Script script = scriptRepo.get(scriptId);
        log.info("Script [{}] described as [{}] executed!", script.getPath(), script.getName());

        return engine.eval(script.getCode(), binding);
    }

    private void resetEngine() {

    }
}
