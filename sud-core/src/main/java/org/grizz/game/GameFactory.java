package org.grizz.game;

import org.grizz.game.config.GameConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by tomasz.bielaszewski on 2015-04-28.
 */
public class GameFactory {
    private static Game INSTANCE;

    public static Game getInstance() {
        if (INSTANCE == null) {
            ConfigurableApplicationContext context = SpringApplication.run(GameConfig.class);
            INSTANCE = context.getBean(Game.class);
        }

        return INSTANCE;
    }
}
