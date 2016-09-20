package org.grizz.game.exception;

public class NotEnoughItemsException extends GameException {
    public NotEnoughItemsException(String message) {
        super(message);
    }

    public NotEnoughItemsException(String message, String... params) {
        super(message, params);
    }
}
