package org.grizz.game.exception;

/**
 * Created by Grizz on 2015-08-17.
 */
public class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
