package org.grizz.game.command.executors;

import org.grizz.game.exception.UnknownCommandException;
import org.springframework.stereotype.Component;

@Component
public class UnknownCommandExecutor {
    public void execute(String command) {
        throw new UnknownCommandException("unknown.command.invoked", command);
    }
}
