package org.grizz.game.exception;

/**
 * Created by Grizz on 2015-08-16.
 */
public class NoSuchScriptException extends GameException {
    public NoSuchScriptException(String message) {
        super(message);
    }

    public NoSuchScriptException(String message, String... params) {
        super(message, params);
    }
}
