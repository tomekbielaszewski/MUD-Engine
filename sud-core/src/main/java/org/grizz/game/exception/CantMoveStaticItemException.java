package org.grizz.game.exception;

/**
 * Created by Grizz on 2015-08-23.
 */
public class CantMoveStaticItemException extends GameException {
    public CantMoveStaticItemException(String message) {
        super(message);
    }

    public CantMoveStaticItemException(String message, Throwable cause) {
        super(message, cause);
    }
}
