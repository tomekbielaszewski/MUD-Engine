package org.grizz.game.exception;

/**
 * Created by Grizz on 2015-08-16.
 */
public class NoSuchLocationException extends GameException {
    public NoSuchLocationException(String message) {
        super(message);
    }

    public NoSuchLocationException(String message, String... params) {
        super(message, params);
    }
}
