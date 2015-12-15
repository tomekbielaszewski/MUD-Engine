package org.grizz.game.exception;

/**
 * Created by Grizz on 2015-08-16.
 */
public class NoSuchItemException extends GameException {
    public NoSuchItemException(String message) {
        super(message);
    }

    public NoSuchItemException(String message, String... params) {
        super(message, params);
    }
}
