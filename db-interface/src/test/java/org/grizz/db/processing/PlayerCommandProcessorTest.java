package org.grizz.db.processing;

import org.grizz.game.Game;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


@RunWith(MockitoJUnitRunner.class)
public class PlayerCommandProcessorTest {
    @Mock
    private Game game;
    @Mock
    private ResponseCollector responseCollector;
    @Mock
    private PlayerResponseFormatter playerResponseFormatter;

    @InjectMocks
    private PlayerCommandProcessor playerCommandProcessor = new PlayerCommandProcessor();

    @Test
    public void name() throws Exception {
        throw new NotImplementedException();
    }
}