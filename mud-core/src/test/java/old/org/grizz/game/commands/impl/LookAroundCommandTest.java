package old.org.grizz.game.commands.impl;

import com.google.common.collect.Maps;
import old.org.grizz.game.CommandTestUtils;
import old.org.grizz.game.TestContext;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.complex.MovementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.mockito.Mockito.verify;

/**
 * Created by Grizz on 2015-12-07.
 */
@ContextConfiguration(classes = {TestContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class LookAroundCommandTest {
    @Autowired
    @InjectMocks
    private LookAroundCommand command;

    @Mock
    private MovementService movementService;

    @Mock
    private PlayerContext context;

    @Mock
    private PlayerResponse response;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAccept() {
        Map<String, Boolean> commands = Maps.newHashMap();//spojrz;rozejrz sie;gdzie jestem?;gdzie ja jestem?;gdzie jestem;gdzie ja jestem;co to za miejsce;co to za miejsce?;lokalizacja

        commands.put("spojrz", Boolean.TRUE);
        commands.put("rozejrz sie", Boolean.TRUE);
        commands.put("gdzie jestem?", Boolean.TRUE);
        commands.put("gdzie ja jestem?", Boolean.TRUE);
        commands.put("gdzie jestem", Boolean.TRUE);
        commands.put("gdzie ja jestem", Boolean.TRUE);
        commands.put("co to za miejsce?", Boolean.TRUE);
        commands.put("co to za miejsce", Boolean.TRUE);
        commands.put("lokalizacja", Boolean.TRUE);

        commands.put("", Boolean.FALSE);

        CommandTestUtils.testCommands(commands, command::accept);
    }

    @Test
    public void testExecute1() {
        command.execute("spojrz", context, response);
        verify(movementService).showCurrentLocation(context, response);
    }

    @Test
    public void testExecute2() {
        command.execute("rozejrz sie", context, response);
        verify(movementService).showCurrentLocation(context, response);
    }

    @Test
    public void testExecute3() {
        command.execute("gdzie jestem?", context, response);
        verify(movementService).showCurrentLocation(context, response);
    }

    @Test
    public void testExecute4() {
        command.execute("gdzie ja jestem?", context, response);
        verify(movementService).showCurrentLocation(context, response);
    }

    @Test
    public void testExecute5() {
        command.execute("gdzie jestem", context, response);
        verify(movementService).showCurrentLocation(context, response);
    }

    @Test
    public void testExecute6() {
        command.execute("gdzie ja jestem", context, response);
        verify(movementService).showCurrentLocation(context, response);
    }

    @Test
    public void testExecute7() {
        command.execute("co to za miejsce?", context, response);
        verify(movementService).showCurrentLocation(context, response);
    }

    @Test
    public void testExecute8() {
        command.execute("co to za miejsce", context, response);
        verify(movementService).showCurrentLocation(context, response);
    }

    @Test
    public void testExecute9() {
        command.execute("lokalizacja", context, response);
        verify(movementService).showCurrentLocation(context, response);
    }
}