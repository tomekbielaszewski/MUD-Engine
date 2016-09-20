package old.org.grizz.game.exception;

public class NoSuchItemException extends GameException {
    public NoSuchItemException(String message) {
        super(message);
    }

    public NoSuchItemException(String message, String... params) {
        super(message, params);
    }
}
