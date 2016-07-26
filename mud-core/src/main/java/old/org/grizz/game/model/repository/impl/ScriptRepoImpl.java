package old.org.grizz.game.model.repository.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.exception.NoSuchScriptException;
import old.org.grizz.game.model.Script;
import old.org.grizz.game.model.repository.ScriptRepo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Grizz on 2015-04-21.
 */
@Slf4j
@Service
public class ScriptRepoImpl implements ScriptRepo {
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
