package org.grizz.game.exception;

public class CantDropStaticItemException extends GameException {
    public CantDropStaticItemException(String message) {
        super(message);
    }

    public CantDropStaticItemException(String message, String... params) {
        super(message, params);
    }
}
