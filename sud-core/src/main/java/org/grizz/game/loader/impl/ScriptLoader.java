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
import java.util.List;

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
                .forEach(path -> {
                    if (path.toString().endsWith("json")) {
                        ScriptEntity[] scriptsArray = null;
                        try {
                            log.info("Reading: {}", path.toString());
                            scriptsArray = gson.fromJson(Files.newBufferedReader(path), ScriptEntity[].class);
                            for (ScriptEntity script : scriptsArray) {
                                scriptRepo.add(loadScriptCode(script));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private Script loadScriptCode(ScriptEntity script) throws IOException, URISyntaxException {
        List<String> sourceCodeBytes = Files.readAllLines(FileUtils.getFilepath(script.getPath()));
        String sourceCode = sourceCodeBytes.stream().reduce((s, s2) -> s + "\n" + s2).orElseGet(() -> "");
        script.setCode(sourceCode);
        return script;
    }
}
