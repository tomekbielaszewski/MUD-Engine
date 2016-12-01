package org.grizz.game.exception;

public class CantMoveStaticItemException extends GameException {
    public CantMoveStaticItemException(String message) {
        super(message);
    }

    public CantMoveStaticItemException(String message, String... params) {
        super(message, params);
    }
}
