package org.grizz.game;

import org.grizz.game.config.GameConfig;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.ui.OutputFormatter;
import org.grizz.game.ui.impl.OutputFormatterImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by Grizz on 2015-04-17.
 */
public class Starter {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GameConfig.class);
        OutputFormatter formatter = new OutputFormatterImpl();

        Game game = context.getBean(Game.class);

        PlayerResponse result = game.runCommand("south", "Grizz");

        System.out.println(formatter.format(result));

        result = game.runCommand("north", "Grizz");

        System.out.println(formatter.format(result));
    }
}
