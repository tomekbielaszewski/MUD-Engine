package org.grizz.game.exception;

public class GameScriptException extends GameException {
    public GameScriptException(String message) {
        super(message);
    }

    public GameScriptException(String message, String... params) {
        super(message, params);
    }
}
