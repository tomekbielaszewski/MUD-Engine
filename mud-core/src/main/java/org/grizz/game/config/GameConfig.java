package org.grizz.game.config;

import org.grizz.game.loader.Loader;
import org.grizz.game.loader.impl.ItemLoader;
import org.grizz.game.loader.impl.LocationLoader;
import org.grizz.game.loader.impl.ScriptLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Configuration
@ComponentScan("org.grizz.game")
@EnableMongoRepositories("org.grizz.game.model.repository")
@PropertySources({
        @PropertySource("${ext.properties.dir:file:}assets/assets.properties"),
        @PropertySource("${ext.properties.dir:file:}assets/strings.properties"),
        @PropertySource("${ext.properties.dir:file:}assets/command-mapping.properties"),
        @PropertySource("${ext.properties.dir:file:}assets/script-runner.properties")
})
public class GameConfig {
    private static final String ASSETS_JSON_PATH_LOCATIONS = "assets.json.path.locations";
    private static final String ASSETS_JSON_PATH_ITEMS = "assets.json.path.items";
    private static final String ASSETS_JSON_PATH_SCRIPTS = "assets.json.path.scripts";

    @Autowired
    private Environment env;

    @PostConstruct
    public void initGame() {
        scriptLoader().load();
        itemLoader().load();
        locationLoader().load();
    }

    @Bean
    public ScriptEngine scriptEngine() {
        return new ScriptEngineManager().getEngineByName("nashorn");
    }

    @Bean
    public Loader itemLoader() {
        return new ItemLoader(env.getProperty(ASSETS_JSON_PATH_ITEMS));
    }

    @Bean
    public Loader locationLoader() {
        return new LocationLoader(env.getProperty(ASSETS_JSON_PATH_LOCATIONS));
    }

    @Bean
    public Loader scriptLoader() {
        return new ScriptLoader(env.getProperty(ASSETS_JSON_PATH_SCRIPTS));
    }
}
