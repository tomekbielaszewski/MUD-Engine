package old.org.grizz.game.exception;

public class CantGoThereException extends GameException {
    public CantGoThereException(String message) {
        super(message);
    }

    public CantGoThereException(String message, String... params) {
        super(message, params);
    }
}
