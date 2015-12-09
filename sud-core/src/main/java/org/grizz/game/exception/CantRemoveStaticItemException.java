package org.grizz.game.exception;

/**
 * Created by Grizz on 2015-12-09.
 */
public class CantRemoveStaticItemException extends GameException {
    public CantRemoveStaticItemException(String message) {
        super(message);
    }

    public CantRemoveStaticItemException(String message, String... params) {
        super(message, params);
    }
}
