package org.grizz.game.exception;

public class CantGiveStaticItemException extends GameException {
    public CantGiveStaticItemException(String message) {
        super(message);
    }

    public CantGiveStaticItemException(String message, String... params) {
        super(message, params);
    }
}
