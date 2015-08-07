package org.grizz.game.loader.impl;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.Script;
import org.grizz.game.model.impl.ScriptEntity;
import org.grizz.game.model.repository.Repository;
import org.grizz.game.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Grizz on 2015-04-17.
 */
@Slf4j
public class ScriptLoader implements Loader {
    private final String _path;

    @Autowired
    private Repository<Script> scriptRepo;

    public ScriptLoader(String path) {
        this._path = path;
    }

    @Override
    @SneakyThrows
    public void load() {
        readScripts();
    }

    private void readScripts() throws IOException, URISyntaxException {
        Gson gson = new Gson();
        FileUtils.listFilesInFolder(_path)
                .stream()
                .filter(path -> path.endsWith(".json"))
                .forEach(path -> {
                    ScriptEntity[] scriptsArray = null;
                    try {
                        log.info("Reading: {}", path.toString());
                        scriptsArray = gson.fromJson(Files.newBufferedReader(path), ScriptEntity[].class);
                        for (ScriptEntity script : scriptsArray) {
                            scriptRepo.add(loadScriptCode(script));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private Script loadScriptCode(ScriptEntity script) throws IOException {
        byte[] sourceCodeBytes = Files.readAllBytes(Paths.get(script.getCode()));
        String sourceCode = String.valueOf(sourceCodeBytes);
        script.setCode(sourceCode);
        return script;
    }
}
