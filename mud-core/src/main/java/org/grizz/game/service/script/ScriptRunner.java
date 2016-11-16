package org.grizz.game.service.script;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.GameScriptException;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@Service
public class ScriptRunner {
    private static final String SCRIPT_TO_RUN = "scriptId";
    private static final String LOGGER_BINDING = "logger";

    @Autowired
    private ScriptRepo scriptRepo;
    @Autowired
    private ScriptBindingProvider bindingProvider;
    @Autowired
    private ScriptEngine engine;

    @Value("${script.engine.master.script.id}")
    private String masterScriptId;
    private Script masterScript;

    @PostConstruct
    public void init() {
        masterScript = scriptRepo.get(masterScriptId);
    }

    public void execute(Script script, Player player, PlayerResponse response, ScriptBinding... bindings) {
        execute(script, player, response, Object.class, bindings);
    }

    @SuppressWarnings("unchecked")
    public <T> T execute(Script script, Player player, PlayerResponse response, Class<T> classOf, ScriptBinding... additionalBindings) {
        List<ScriptBinding> bindings = Lists.newArrayList(additionalBindings);
        bindings.addAll(bindingProvider.provide(player, response));

        return (T) eval(script, getSimpleBindings(bindings));
    }

    private Object eval(Script script, SimpleBindings bindings) {
        bindings.put(SCRIPT_TO_RUN, script.getId());
        bindings.put(LOGGER_BINDING, log);

        try {
            return engine.eval(masterScriptReader(), bindings);
        } catch (ScriptException e) {
            throw new GameScriptException(e.getMessage(), e.getMessage(), e.getFileName(), "" + e.getLineNumber(), "" + e.getColumnNumber());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedReader masterScriptReader() throws IOException {
        return Files.newBufferedReader(FileUtils.getFilepath(masterScript.getPath()), StandardCharsets.UTF_8);
    }

    private SimpleBindings getSimpleBindings(List<ScriptBinding> bindings) {
        SimpleBindings simpleBindings = new SimpleBindings();
        for (ScriptBinding binding : bindings) {
            simpleBindings.put(binding.getName(), binding.getObject());
        }
        return simpleBindings;
    }


}
