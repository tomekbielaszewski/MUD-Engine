package old.org.grizz.game.exception;

/**
 * Created by Grizz on 2015-08-23.
 */
public class CantMoveStaticItemException extends GameException {
    public CantMoveStaticItemException(String message) {
        super(message);
    }

    public CantMoveStaticItemException(String message, String... params) {
        super(message, params);
    }
}
