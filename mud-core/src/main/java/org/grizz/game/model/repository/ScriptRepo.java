package org.grizz.game.model.repository;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.NoSuchScriptException;
import org.grizz.game.model.Script;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ScriptRepo implements Repository<Script> {
    private Map<String, Script> scripts = Maps.newHashMap();

    @Override
    public void add(Script script) {
        log.info("ScriptRepo.add({})", script);

        if (scripts.containsKey(script.getId())) {
            throw new IllegalArgumentException("Script ID[" + script.getId() + "] is duplicated");
        }

        scripts.put(script.getId(), script);
    }

    @Override
    public Script get(String id) {
        if (scripts.containsKey(id)) {
            return scripts.get(id);
        } else {
            throw new NoSuchScriptException("no.such.script", id);
        }
    }
}
