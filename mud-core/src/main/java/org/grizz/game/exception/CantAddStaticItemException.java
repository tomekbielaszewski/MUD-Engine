package org.grizz.game.exception;

public class CantAddStaticItemException extends GameException {
    public CantAddStaticItemException(String message, String... params) {
        super(message, params);
    }
}
