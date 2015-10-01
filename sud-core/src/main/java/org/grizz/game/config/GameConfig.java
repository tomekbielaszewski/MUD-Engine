package org.grizz.game.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.loader.impl.ItemLoader;
import org.grizz.game.loader.impl.LocationLoader;
import org.grizz.game.loader.impl.MobLoader;
import org.grizz.game.loader.impl.ScriptLoader;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.impl.items.ItemStackEntity;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
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
    private PlayerRepository playerRepository;

    @Autowired
    private LocationRepo locationRepo;

    @PostConstruct
    public void initGame() {
        itemLoader().load();
        mobLoader().load();
        scriptLoader().load();
        locationLoader().load();

    /*
        Po zaimplementowaniu komunikacji z bazą - usunąć
     */
        playerRepository.add(
                PlayerContextImpl.builder()
                        .name("Grizz")
                        .currentLocation("1")
                        .equipment(Lists.newArrayList())
//                        .equipment(Lists.newArrayList(
//                                ItemStackEntity.builder()
//                                        .itemId("3") //brazowa moneta
//                                        .quantity(54)
//                                        .build(),
//                                ItemStackEntity.builder()
//                                        .itemId("2") //srebrna moneta
//                                        .quantity(21)
//                                        .build(),
//                                ItemStackEntity.builder()
//                                        .itemId("1") //złota moneta
//                                        .quantity(3)
//                                        .build(),
//                                ItemStackEntity.builder()
//                                        .itemId("100") //krótki miecz
//                                        .quantity(1)
//                                        .build(),
//                                ItemStackEntity.builder()
//                                        .itemId("5") //patelnia
//                                        .quantity(1)
//                                        .build()
//                        ))
                        .parameters(Maps.newHashMap())
                        .build()
        );
        playerRepository.add(
                PlayerContextImpl.builder()
                        .name("Nebu")
                        .currentLocation("1")
                        .equipment(Lists.newArrayList())
                        .parameters(Maps.newHashMap())
                        .build()
        );
        playerRepository.add(
                PlayerContextImpl.builder()
                        .name("Lothar")
                        .currentLocation("1")
                        .equipment(Lists.newArrayList())
                        .parameters(Maps.newHashMap())
                        .build()
        );
        locationRepo.get("1").getItems().addAll(Lists.newArrayList(
                ItemStackEntity.builder()
                        .itemId("3")
                        .quantity(2)
                        .build(),
                ItemStackEntity.builder()
                        .itemId("203")
                        .quantity(1)
                        .build(),
                ItemStackEntity.builder()
                        .itemId("200")
                        .quantity(1)
                        .build(),
                ItemStackEntity.builder()
                        .itemId("103")
                        .quantity(1)
                        .build()
        ));
        locationRepo.get("1").getStaticItems().addAll(Lists.newArrayList(
                ItemStackEntity.builder()
                        .itemId("301")
                        .quantity(1)
                        .build(),
                ItemStackEntity.builder()
                        .itemId("302")
                        .quantity(1)
                        .build()
        ));
        locationRepo.get("3").getItems().addAll(Lists.newArrayList(
                ItemStackEntity.builder()
                        .itemId("7")
                        .quantity(1)
                        .build()
        ));
        locationRepo.get("4").getItems().addAll(Lists.newArrayList(
                ItemStackEntity.builder()
                        .itemId("201")
                        .quantity(1)
                        .build(),
                ItemStackEntity.builder()
                        .itemId("202")
                        .quantity(1)
                        .build(),
                ItemStackEntity.builder()
                        .itemId("100")
                        .quantity(1)
                        .build(),
                ItemStackEntity.builder()
                        .itemId("1")
                        .quantity(5)
                        .build()
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
