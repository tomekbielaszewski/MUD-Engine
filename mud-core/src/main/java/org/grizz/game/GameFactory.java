package org.grizz.game;

import org.grizz.game.config.GameConfig;
import org.grizz.game.service.notifier.Notifier;
import org.grizz.game.service.notifier.ProxyNotifier;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class GameFactory {
    private static Game INSTANCE;

    public static Game getInstance() {
        if (INSTANCE == null) {
            ConfigurableApplicationContext context = SpringApplication.run(GameConfig.class);
            INSTANCE = context.getBean(Game.class);
        }

        return INSTANCE;
    }

    public static Game getInstance(Notifier customNotifier) {
        if (INSTANCE == null) {
            System.out.println();
            ConfigurableApplicationContext context = SpringApplication.run(GameConfig.class);
            context.getBean("proxyNotifier", ProxyNotifier.class)
                    .setNotifier(customNotifier);
            INSTANCE = context.getBean(Game.class);
        }

        return INSTANCE;
    }
}
