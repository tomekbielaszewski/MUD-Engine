package org.grizz.game.utils;

import org.grizz.game.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by Grizz on 2015-08-17.
 */
@Component
public class GameExceptionHandler {
    @Autowired
    private Environment env;

    public String handle(GameException e) {
        String message = e.getMessage();
        Throwable cause = e.getCause();
        String causeMessage = cause != null ? cause.getMessage() : "";
        String formattedMessage = String.format(env.getProperty(message), causeMessage);
        return formattedMessage;
    }
}
