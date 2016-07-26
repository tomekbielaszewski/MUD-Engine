package old.org.grizz.game.service.complex.impl;

import old.org.grizz.game.model.Location;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.model.impl.PlayerResponseImpl;
import old.org.grizz.game.model.repository.PlayerRepository;
import old.org.grizz.game.service.complex.MultiplayerNotificationService;
import old.org.grizz.game.service.simple.Notifier;
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
        PlayerResponseImpl response = new PlayerResponseImpl();
        response.getPlayerEvents().add(event);
        proxyNotifier.notify(player.getName(), response);
    }

    @Override
    public void send(PlayerContext player, PlayerResponse response) {
        proxyNotifier.notify(player.getName(), response);
    }
}
