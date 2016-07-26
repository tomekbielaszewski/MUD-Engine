package old.org.grizz.game.exception;

/**
 * Created by Grizz on 2015-08-16.
 */
public class NotEnoughItemsException extends GameException {
    public NotEnoughItemsException(String message) {
        super(message);
    }

    public NotEnoughItemsException(String message, String... params) {
        super(message, params);
    }
}
