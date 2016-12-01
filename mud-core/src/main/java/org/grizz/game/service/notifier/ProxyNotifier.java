package org.grizz.game.service.notifier;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.PlayerResponse;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProxyNotifier implements Notifier {
    private Notifier actualNotifier = (playerName, response) -> { };

    public void setNotifier(Notifier notifier) {
        log.info("Using {} as notifier", notifier.getClass().getName());
        this.actualNotifier = notifier;
    }

    @Override
    public void notify(String playerName, PlayerResponse response) {
        actualNotifier.notify(playerName, response);
    }
}
