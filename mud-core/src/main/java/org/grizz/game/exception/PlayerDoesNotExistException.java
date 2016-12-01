package org.grizz.game.exception;

public class PlayerDoesNotExistException extends GameException {
    public PlayerDoesNotExistException(String message) {
        super(message);
    }

    public PlayerDoesNotExistException(String message, String... params) {
        super(message, params);
    }
}
