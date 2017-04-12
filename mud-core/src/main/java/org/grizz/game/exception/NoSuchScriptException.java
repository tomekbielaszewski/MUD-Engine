package org.grizz.game.exception;

public class NoSuchScriptException extends GameException {
    public NoSuchScriptException(String message, String... params) {
        super(message, params);
    }
}
