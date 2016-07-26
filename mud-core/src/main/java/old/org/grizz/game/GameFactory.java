package old.org.grizz.game;

import old.org.grizz.game.config.GameConfig;
import old.org.grizz.game.service.simple.Notifier;
import old.org.grizz.game.service.simple.impl.ProxyNotifier;
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

    public static Game getInstance(Notifier customNotifier) {
        if (INSTANCE == null) {
            ConfigurableApplicationContext context = SpringApplication.run(GameConfig.class);
            Notifier playerNotifier = context.getBean("proxyNotifier", Notifier.class);
            ((ProxyNotifier) playerNotifier).setNotifier(customNotifier);
            INSTANCE = context.getBean(Game.class);
        }

        return INSTANCE;
    }
}
