package old.org.grizz.game.exception;

public class InvalidAmountException extends GameException {
    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, String... params) {
        super(message, params);
    }
}
