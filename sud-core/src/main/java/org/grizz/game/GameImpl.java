package org.grizz.game;

import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-04-17.
 */
@Service
public class GameImpl implements Game {
    @Override
    public String runCommand(String command, String player) {
        return String.format("Uzyles komendy [%s] jako [%s]", command, player);
    }
}
