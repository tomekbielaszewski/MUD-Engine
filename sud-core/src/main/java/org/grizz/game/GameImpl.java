package org.grizz.game;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.grizz.game.commands.CommandHandlerBus;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.EquipmentEntity;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-04-17.
 */
@Service
public class GameImpl implements Game {
    @Autowired
    private CommandHandlerBus commandHandlerBus;
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public PlayerResponse runCommand(String command, String playerName) {
        PlayerContext player = playerRepository.findByName(playerName);
        if (player == null) {
            PlayerContextImpl player_ = PlayerContextImpl.builder()
                    .name(playerName)

                    .strength(10)
                    .dexterity(10)
                    .endurance(10)
                    .intelligence(10)
                    .wisdom(10)
                    .charisma(10)

                    .equipment(EquipmentEntity.builder()
                            .backpack(Lists.newArrayList())
                            .build())

                    .currentLocation("1")
                    .pastLocation("1")

                    .parameters(Maps.newHashMap())

                    .build();
            player = playerRepository.insert(player_);
        }

        PlayerResponseImpl response = (PlayerResponseImpl) commandHandlerBus.execute(command, player);
        playerRepository.save((PlayerContextImpl) player);
        return response;
    }
}
