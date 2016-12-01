package org.grizz.game.exception;

import lombok.Getter;

public class GameException extends RuntimeException {
    @Getter
    protected String[] params;

    public GameException(String message) {
        super(message);
        this.params = new String[]{};
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
        this.params = new String[]{};
    }

    public GameException(String message, String... params) {
        super(message);
        this.params = params;
    }
}
