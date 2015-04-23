import org.grizz.game.Game;
import org.grizz.game.config.GameConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by Grizz on 2015-04-17.
 */
public class Starter {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GameConfig.class);
        Game game = context.getBean(Game.class);

        String result = game.runCommand("rozejrzyj sie", "Grizwold");
        System.out.println(result);
    }
}
