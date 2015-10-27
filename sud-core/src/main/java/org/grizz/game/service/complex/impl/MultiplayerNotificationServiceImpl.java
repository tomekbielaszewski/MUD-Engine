package org.grizz.game.service.complex.impl;

import org.grizz.game.model.Location;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.complex.MultiplayerNotificationService;
import org.grizz.game.service.simple.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Grizz on 2015-10-27.
 */
@Service
public class MultiplayerNotificationServiceImpl implements MultiplayerNotificationService {
    @Autowired
    private Notifier proxyNotifier;
    @Autowired
    private PlayerRepository playerRepo;

    @Override
    public void broadcast(Location location, String event, PlayerContext sender) {
        List<PlayerContext> players = playerRepo.findByCurrentLocation(location.getId());

        for (PlayerContext player : players) {
            if (!sender.getName().equals(player.getName())) {
                send(player, event);
            }
        }
    }

    @Override
    public void send(PlayerContext player, String event) {
        proxyNotifier.notify(player.getName(), event);
    }
}
