package old.org.grizz.game.exception;

/**
 * Created by Grizz on 2015-08-23.
 */
public class CantGiveStaticItemException extends GameException {
    public CantGiveStaticItemException(String message) {
        super(message);
    }

    public CantGiveStaticItemException(String message, String... params) {
        super(message, params);
    }
}
