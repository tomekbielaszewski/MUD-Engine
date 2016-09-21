package org.grizz.game.command.parsers.admin;

import org.grizz.game.command.executors.admin.AdminPutItemCommandExecutor;
import org.grizz.game.command.executors.admin.AdminShowPlayerListCommandExecutor;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminShowPlayerListCommandTest {
    @Mock
    private Environment environment;
    @Mock
    private AdminShowPlayerListCommandExecutor adminCommandExecutor;

    @InjectMocks
    private AdminShowPlayerListCommand command = new AdminShowPlayerListCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("lista graczy");
    }

    @Test
    public void testAccept() throws Exception {
        assertTrue(command.accept("lista graczy"));
    }

    @Test
    public void testExecute() throws Exception {
        Player player = Player.builder().build();
        PlayerResponse response = new PlayerResponse();

        command.execute("lista graczy", player, response);
        verify(adminCommandExecutor).showPlayerList(player, response);
        verifyNoMoreInteractions(adminCommandExecutor);
    }
}