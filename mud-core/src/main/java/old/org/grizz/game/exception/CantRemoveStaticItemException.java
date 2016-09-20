package old.org.grizz.game.exception;

public class CantRemoveStaticItemException extends GameException {
    public CantRemoveStaticItemException(String message) {
        super(message);
    }

    public CantRemoveStaticItemException(String message, String... params) {
        super(message, params);
    }
}
