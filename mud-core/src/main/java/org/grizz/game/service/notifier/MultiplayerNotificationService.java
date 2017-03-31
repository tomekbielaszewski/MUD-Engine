package org.grizz.game.service.notifier;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MultiplayerNotificationService {
    @Autowired
    private Notifier proxyNotifier;
    @Autowired
    private PlayerRepository playerRepository;

    public void broadcast(Location location, String event, Player sender) {
        List<Player> players = playerRepository.findByCurrentLocation(location.getId());

        for (Player player : players) {
            if (!sender.getName().equals(player.getName())) {
                send(player, event);
            }
        }
    }

    public void send(Player player, String event) {
        PlayerResponse response = new PlayerResponse();
        response.getPlayerEvents().add(event);
        log.debug("{}: {}", player.getName(), response.toString());
        proxyNotifier.notify(player.getName(), response);
    }

    public void send(Player player, PlayerResponse response) {
        proxyNotifier.notify(player.getName(), response);
    }
}
