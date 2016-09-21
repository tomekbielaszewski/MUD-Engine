package org.grizz.game.exception;

import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GameExceptionHandlerTest {

    @Test
    public void testHandle() throws Exception {
        throw new NotImplementedException();
    }
}