package org.grizz.db;

import org.grizz.db.config.MainConfig;
import org.grizz.db.model.PlayerCommand;
import org.grizz.db.model.repository.PlayerCommandRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

public class Starter {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainConfig.class);
        PlayerCommandRepository playerCommandRepository = context.getBean(PlayerCommandRepository.class);

        System.out.println("Type your commands:");
        String command;

        try (Scanner sc = new Scanner(System.in)) {
            while (!(command = sc.nextLine()).equals("q")) {
                PlayerCommand playerCommand = PlayerCommand.builder()
                        .command(command)
                        .player("Grizwold")
                        .processed(false)
                        .timestamp(System.currentTimeMillis())
                        .build();
                playerCommandRepository.insert(playerCommand);
            }
            context.close();
        }
    }
}
