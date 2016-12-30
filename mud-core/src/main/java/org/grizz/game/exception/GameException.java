package org.grizz.game.exception;

import lombok.Getter;

import java.util.Arrays;

public class GameException extends RuntimeException {
    @Getter
    protected final String[] params;

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

    @Override
    public String toString() {
        return super.toString() + " " + Arrays.toString(params);
    }
}
