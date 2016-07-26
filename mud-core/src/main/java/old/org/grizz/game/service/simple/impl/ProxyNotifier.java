package old.org.grizz.game.service.simple.impl;

import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.simple.Notifier;

/**
 * Created by Grizz on 2015-10-27.
 */
@Slf4j
public class ProxyNotifier implements Notifier {
    private Notifier delegatedNotifier;

    public ProxyNotifier(Notifier notifier) {
        log.info("Using {} as notifier", notifier.getClass().getName());
        this.delegatedNotifier = notifier;
    }

    public void setNotifier(Notifier notifier) {
        log.info("Using {} as notifier", notifier.getClass().getName());
        this.delegatedNotifier = notifier;
    }

    @Override
    public void notify(String playerName, PlayerResponse response) {
        delegatedNotifier.notify(playerName, response);
    }
}
