package org.grizz.game.exception;

public class InsufficientPermissionException extends GameException {
    public InsufficientPermissionException(String message) {
        super(message);
    }

    public InsufficientPermissionException(String message, String... params) {
        super(message, params);
    }
}
