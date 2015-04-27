package org.grizz.game.model.repository.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Grizz on 2015-04-27.
 */
@Slf4j
@Service
public class PlayerRepositoryImpl implements PlayerRepository {
    private Map<String, PlayerContext> playerContexts = Maps.newHashMap();

    @Override
    public void add(PlayerContext playerContext) {
        log.info("playerRepo.add({})", playerContext);

        if (playerContexts.containsKey(playerContext.getName())) {
            throw new IllegalArgumentException("Player name[" + playerContext.getName() + "] is duplicated");
        }

        playerContexts.put(playerContext.getName(), playerContext);
    }

    @Override
    public PlayerContext get(String name) {
        if (playerContexts.containsKey(name)) {
            return playerContexts.get(name);
        } else {
            throw new IllegalArgumentException("No such player: " + name);
        }
    }
}
