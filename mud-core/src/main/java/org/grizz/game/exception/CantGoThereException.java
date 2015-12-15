package org.grizz.game.exception;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
public class CantGoThereException extends GameException {
    public CantGoThereException(String message) {
        super(message);
    }

    public CantGoThereException(String message, String... params) {
        super(message, params);
    }
}
