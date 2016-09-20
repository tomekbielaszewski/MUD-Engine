package org.grizz.game.loader.impl;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.exception.ScriptLoadingException;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.Repository;
import org.grizz.game.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

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
                .forEach(path -> {
                    if (path.toString().endsWith("json")) {
                        Script[] scriptsArray = null;
                        try {
                            log.info("Reading: {}", path.toString());
                            scriptsArray = gson.fromJson(Files.newBufferedReader(path), Script[].class);
                            for (Script script : scriptsArray) {
                                checkScriptPath(script);
                                scriptRepo.add(script);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void checkScriptPath(Script script) {
        String path = script.getPath();
        try {
            FileUtils.getFilepath(path);
        } catch (IOException e) {
            throw new ScriptLoadingException("Problem loading script file [" + path + "]", e);
        }
    }
}
