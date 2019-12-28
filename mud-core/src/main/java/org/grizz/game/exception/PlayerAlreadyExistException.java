package org.grizz.game.exception;

public class PlayerAlreadyExistException extends GameException {
    public PlayerAlreadyExistException(String message, String... params) {
        super(message, params);
    }
}
