package org.grizz.game.exception;

public class LocationAlreadyExistException extends GameException {
    public LocationAlreadyExistException(String message, String... params) {
        super(message, params);
    }
}
