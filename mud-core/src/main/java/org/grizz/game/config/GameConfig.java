package org.grizz.game.config;

import old.org.grizz.game.service.simple.Notifier;
import old.org.grizz.game.service.simple.impl.DefaultNotifier;
import old.org.grizz.game.service.simple.impl.ProxyNotifier;
import org.grizz.game.loader.Loader;
import org.grizz.game.loader.impl.ItemLoader;
import org.grizz.game.loader.impl.LocationLoader;
import org.grizz.game.loader.impl.MobLoader;
import org.grizz.game.loader.impl.ScriptLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Configuration
@PropertySources({
        @PropertySource("assets.properties"),
        @PropertySource("strings.properties"),
        @PropertySource("command-mapping.properties")
})
public class GameConfig {
    private static final String ASSETS_JSON_PATH_LOCATIONS = "assets.json.path.locations";
    private static final String ASSETS_JSON_PATH_ITEMS = "assets.json.path.items";
    private static final String ASSETS_JSON_PATH_MOBS = "assets.json.path.mobs";
    private static final String ASSETS_JSON_PATH_SCRIPTS = "assets.json.path.scripts";

    @Autowired
    private Environment env;

    @PostConstruct
    public void initGame() {
        scriptLoader().load();
        itemLoader().load();
        mobLoader().load();
        locationLoader().load();
    }

    @Bean
    public Notifier defaultNotifier() {
        return new DefaultNotifier();
    }

    @Bean
    public Notifier proxyNotifier() {
        return new ProxyNotifier(defaultNotifier());
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
    public Loader mobLoader() {
        return new MobLoader(env.getProperty(ASSETS_JSON_PATH_MOBS));
    }

    @Bean
    public Loader scriptLoader() {
        return new ScriptLoader(env.getProperty(ASSETS_JSON_PATH_SCRIPTS));
    }
}
