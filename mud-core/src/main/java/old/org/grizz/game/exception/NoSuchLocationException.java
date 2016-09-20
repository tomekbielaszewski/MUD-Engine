package old.org.grizz.game.exception;

public class NoSuchLocationException extends GameException {
    public NoSuchLocationException(String message) {
        super(message);
    }

    public NoSuchLocationException(String message, String... params) {
        super(message, params);
    }
}
