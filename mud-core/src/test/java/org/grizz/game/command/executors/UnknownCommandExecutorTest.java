package org.grizz.game.command.executors;

import org.grizz.game.exception.UnknownCommandException;
import org.junit.Test;

public class UnknownCommandExecutorTest {
    private UnknownCommandExecutor unknownCommandExecutor = new UnknownCommandExecutor();

    @Test(expected = UnknownCommandException.class)
    public void throwsUnknownCommandException() throws Exception {
        unknownCommandExecutor.execute("");
    }
}