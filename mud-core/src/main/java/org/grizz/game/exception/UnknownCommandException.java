package org.grizz.game.exception;

public class UnknownCommandException extends GameException {
    public UnknownCommandException(String message) {
        super(message);
    }

    public UnknownCommandException(String message, String... params) {
        super(message, params);
    }
}
