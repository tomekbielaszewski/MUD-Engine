package org.grizz.game.exception;

public class PlayerDoesNotExist extends GameException {
    public PlayerDoesNotExist(String message) {
        super(message);
    }

    public PlayerDoesNotExist(String message, String... params) {
        super(message, params);
    }
}
