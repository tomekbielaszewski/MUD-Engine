package org.grizz.game.exception;

public class CantOwnStaticItemException extends GameException {
    public CantOwnStaticItemException(String message) {
        super(message);
    }

    public CantOwnStaticItemException(String message, String... params) {
        super(message, params);
    }
}
