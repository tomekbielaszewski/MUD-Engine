package org.grizz.game.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.loader.impl.ItemLoader;
import org.grizz.game.loader.impl.LocationLoader;
import org.grizz.game.loader.impl.MobLoader;
import org.grizz.game.loader.impl.ScriptLoader;
import org.grizz.game.model.impl.LocationItemsEntity;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationItemsRepository;
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
    private LocationItemsRepository locationItemsRepository;

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
        LocationItemsEntity location1 = LocationItemsEntity.builder()
                .locationId("1")
                .mobileItems(Lists.newArrayList(
                        itemRepo.getByName("Brązowa moneta"),
                        itemRepo.getByName("Brązowa moneta"),
                        itemRepo.getByName("Napierśnik"),
                        itemRepo.getByName("Lniana koszula"),
                        itemRepo.getByName("Sztylet")))
                .staticItems(Lists.newArrayList(
                        itemRepo.getByName("Kowadło"),
                        itemRepo.getByName("Ołtarz")
                ))
                .build();
        LocationItemsEntity location3 = LocationItemsEntity.builder()
                .locationId("3")
                .mobileItems(Lists.newArrayList(
                        itemRepo.getByName("Zardzewiały klucz")
                ))
                .staticItems(Lists.newArrayList())
                .build();
        LocationItemsEntity location4 = LocationItemsEntity.builder()
                .locationId("4")
                .mobileItems(Lists.newArrayList(
                        itemRepo.getByName("Kolczuga"),
                        itemRepo.getByName("Napierśnik"),
                        itemRepo.getByName("Krótki miecz"),
                        itemRepo.getByName("Złota moneta"),
                        itemRepo.getByName("Złota moneta"),
                        itemRepo.getByName("Złota moneta"),
                        itemRepo.getByName("Złota moneta"),
                        itemRepo.getByName("Złota moneta")
                ))
                .staticItems(Lists.newArrayList())
                .build();
        LocationItemsEntity location8 = LocationItemsEntity.builder()
                .locationId("8")
                .mobileItems(Lists.newArrayList())
                .staticItems(Lists.newArrayList(
                        itemRepo.getByName("Brama teleportacyjna")
                ))
                .build();
        location1.setId(locationItemsRepository.findByLocationId("1").getId());
        locationItemsRepository.save(location1);
        location3.setId(locationItemsRepository.findByLocationId("3").getId());
        locationItemsRepository.save(location3);
        location4.setId(locationItemsRepository.findByLocationId("4").getId());
        locationItemsRepository.save(location4);
        location8.setId(locationItemsRepository.findByLocationId("8").getId());
        locationItemsRepository.save(location8);
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
