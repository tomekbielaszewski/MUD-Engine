package old.org.grizz.game.exception;

/**
 * Created by tomasz.bielaszewski on 2015-04-27.
 */
public class InvalidAmountException extends GameException {
    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, String... params) {
        super(message, params);
    }
}
