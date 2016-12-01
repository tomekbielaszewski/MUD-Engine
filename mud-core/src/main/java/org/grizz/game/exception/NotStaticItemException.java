package org.grizz.game.exception;

public class NotStaticItemException extends GameException {
    public NotStaticItemException(String message) {
        super(message);
    }

    public NotStaticItemException(String message, String... params) {
        super(message, params);
    }
}
