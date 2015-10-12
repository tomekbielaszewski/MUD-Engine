package org.grizz.game.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.loader.impl.ItemLoader;
import org.grizz.game.loader.impl.LocationLoader;
import org.grizz.game.loader.impl.MobLoader;
import org.grizz.game.loader.impl.ScriptLoader;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Hello world!
 */
@Slf4j
@SpringBootApplication
@EnableMongoRepositories("org.grizz.game.model.repository")
@ComponentScan("org.grizz.game")
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

    @Autowired
    private LocationRepo locationRepo;

    @Autowired
    private ItemRepo itemRepo;

    @PostConstruct
    public void initGame() {
        itemLoader().load();
        mobLoader().load();
        scriptLoader().load();
        locationLoader().load();

    /*
        Po zaimplementowaniu komunikacji z bazą - usunąć
     */
        locationRepo.get("1").getItems().getMobileItems().addAll(Lists.newArrayList(
                itemRepo.getByName("Brązowa moneta"),
                itemRepo.getByName("Brązowa moneta"),
                itemRepo.getByName("Napierśnik"),
                itemRepo.getByName("Lniana koszula"),
                itemRepo.getByName("Sztylet")
        ));
        locationRepo.get("1").getItems().getStaticItems().addAll(Lists.newArrayList(
                itemRepo.getByName("Kowadło"),
                itemRepo.getByName("Ołtarz")
        ));
        locationRepo.get("3").getItems().getMobileItems().addAll(Lists.newArrayList(
                itemRepo.getByName("Zardzewiały klucz")
        ));
        locationRepo.get("4").getItems().getMobileItems().addAll(Lists.newArrayList(
                itemRepo.getByName("Kolczuga"),
                itemRepo.getByName("Napierśnik"),
                itemRepo.getByName("Krótki miecz"),
                itemRepo.getByName("Złota moneta"),
                itemRepo.getByName("Złota moneta"),
                itemRepo.getByName("Złota moneta"),
                itemRepo.getByName("Złota moneta"),
                itemRepo.getByName("Złota moneta")
        ));
        locationRepo.get("8").getItems().getStaticItems().addAll(Lists.newArrayList(
                itemRepo.getByName("Brama teleportacyjna")
        ));
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
