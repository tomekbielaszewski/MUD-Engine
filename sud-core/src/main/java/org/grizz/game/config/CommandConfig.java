package org.grizz.game.config;

import org.grizz.game.commands.CommandHandlerBus;
import org.grizz.game.commands.impl.MovementCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
@Configuration
public class CommandConfig {

    @Bean
    @Autowired
    public CommandHandlerBus commandHandlerBus(
            final MovementCommand movementCommand
    ) {
        CommandHandlerBus commandHandlerBus = new CommandHandlerBus();

        commandHandlerBus.addCommand(movementCommand);
        //

        return commandHandlerBus;
    }
}
