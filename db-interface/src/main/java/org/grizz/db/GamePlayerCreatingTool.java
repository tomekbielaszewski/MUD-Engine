package org.grizz.db;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.grizz.game.config.GameConfig;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.repository.PlayerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class GamePlayerCreatingTool {
    public static void main(String[] args) {
        String name = "Grizwold";

        Player player = Player.builder()
                .name(name)
                .currentLocation("1")
                .equipment(Equipment.builder()
                        .backpack(Lists.newArrayList())
                        .build())
                .parameters(Maps.newHashMap())
                .build();

        ConfigurableApplicationContext context = SpringApplication.run(GameConfig.class);
        PlayerRepository playerRepository = context.getBean(PlayerRepository.class);
        Player alreadyExist = playerRepository.findByName(name);
        if (alreadyExist == null) {
            playerRepository.insert(player);
        }
        SpringApplication.exit(context);
    }
}
