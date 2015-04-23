package org.grizz.game.config;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.loader.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Hello world!
 */
@Slf4j
@Configuration
@ComponentScan("org.grizz.game")
@PropertySource("assets.properties")
public class GameConfig {
    private static final String ASSETS_JSON_PATH_LOCATIONS = "assets.json.path.locations";
    private static final String ASSETS_JSON_PATH_LOCATIONS_SCRIPTS = "assets.json.path.locations.scripts";
    private static final String ASSETS_JSON_PATH_ITEMS = "assets.json.path.items";
    private static final String ASSETS_JSON_PATH_MOBS = "assets.json.path.mobs";
    private static final String ASSETS_JSON_PATH_QUESTS = "assets.json.path.quests";

    @Autowired
    private Environment env;

    @PostConstruct
    public void initGame() {
        itemLoader().load();
        mobLoader().load();
        questLoader().load();
        locationLoader().load();
        locationScriptLoader().load();
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
    public Loader locationScriptLoader() {
        return new LocationScriptLoader(env.getProperty(ASSETS_JSON_PATH_LOCATIONS_SCRIPTS));
    }

    @Bean
    public Loader locationLoader() {
        return new LocationLoader(env.getProperty(ASSETS_JSON_PATH_LOCATIONS));
    }

    @Bean
    public Loader mobLoader() {
        return new MobLoader(env.getProperty(ASSETS_JSON_PATH_MOBS));
    }

    @Bean
    public Loader questLoader() {
        return new QuestLoader(env.getProperty(ASSETS_JSON_PATH_QUESTS));
    }
}
