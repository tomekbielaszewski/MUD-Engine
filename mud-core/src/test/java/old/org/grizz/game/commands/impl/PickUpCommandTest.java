package old.org.grizz.game.commands.impl;

import com.google.common.collect.Maps;
import old.org.grizz.game.CommandTestUtils;
import old.org.grizz.game.TestContext;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.complex.PlayerLocationInteractionService;
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
 * Created by Grizz on 2015-08-11.
 */
@ContextConfiguration(classes = {TestContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PickUpCommandTest {
    @Autowired
    @InjectMocks
    private PickUpCommand command;

    @Mock
    private PlayerLocationInteractionService playerLocationInteractionService;

    @Mock
    private PlayerContext context;

    @Mock
    private PlayerResponse response;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAccept() throws Exception {
        Map<String, Boolean> commands = Maps.newHashMap();

        commands.put("podnies mlot", Boolean.TRUE);
        commands.put("podnies mlot 3", Boolean.TRUE);
        commands.put("podnies mlot bojowy", Boolean.TRUE);
        commands.put("podnies mlot bojowy 3", Boolean.TRUE);
        commands.put("podnies mlot bojowy 33", Boolean.TRUE);
        commands.put("podnies mlot\n", Boolean.TRUE);
        commands.put("podnies mlot bojowy\n", Boolean.TRUE);

        commands.put("podnies \n", Boolean.TRUE);//this should never happen - command is trimmed before execution

        commands.put("podnies mlot 3\n", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3\n", Boolean.FALSE);
        commands.put("podnies ", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3 3", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3 3\n", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3 3 3\n", Boolean.FALSE);
        commands.put("podnies mlot bojowy3\n", Boolean.FALSE);
        commands.put("podnies mlot bojowy 3as\n", Boolean.FALSE);

        commands.put("wez mlot", Boolean.TRUE);
        commands.put("wez mlot 3", Boolean.TRUE);
        commands.put("wez mlot bojowy", Boolean.TRUE);
        commands.put("wez mlot bojowy 3", Boolean.TRUE);
        commands.put("wez mlot bojowy 33", Boolean.TRUE);
        commands.put("wez mlot\n", Boolean.TRUE);
        commands.put("wez mlot bojowy\n", Boolean.TRUE);

        commands.put("wez \n", Boolean.TRUE);//this should never happen - command is trimmed before execution

        commands.put("wez mlot 3\n", Boolean.FALSE);
        commands.put("wez mlot bojowy 3\n", Boolean.FALSE);
        commands.put("wez ", Boolean.FALSE);
        commands.put("wez mlot bojowy 3 3", Boolean.FALSE);
        commands.put("wez mlot bojowy 3 3\n", Boolean.FALSE);
        commands.put("wez mlot bojowy 3 3 3\n", Boolean.FALSE);
        commands.put("wez mlot bojowy3\n", Boolean.FALSE);
        commands.put("wez mlot bojowy 3as\n", Boolean.FALSE);

        CommandTestUtils.testCommands(commands, command::accept);
    }

    @Test
    public void testExecute() {
        command.execute("podnies mlot", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot", 1, context, response);
    }

    @Test
    public void testExecute2() {
        command.execute("podnies mlot 3", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot", 3, context, response);
    }

    @Test
    public void testExecute3() {
        command.execute("podnies mlot bojowy", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot bojowy", 1, context, response);
    }

    @Test
    public void testExecute4() {
        command.execute("podnies mlot bojowy 3", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot bojowy", 3, context, response);
    }

    @Test
    public void testExecute5() {
        command.execute("podnies mlot bojowy 33", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot bojowy", 33, context, response);
    }

    @Test
    public void testExecute6() {
        command.execute("podnies mlot\n", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot\n", 1, context, response);
    }

    @Test
    public void testExecute7() {
        command.execute("podnies mlot bojowy\n", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot bojowy\n", 1, context, response);
    }

    @Test
    public void testExecute8() {
        command.execute("wez mlot", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot", 1, context, response);
    }

    @Test
    public void testExecute9() {
        command.execute("wez mlot 3", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot", 3, context, response);
    }

    @Test
    public void testExecute10() {
        command.execute("wez mlot bojowy", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot bojowy", 1, context, response);
    }

    @Test
    public void testExecute11() {
        command.execute("wez mlot bojowy 3", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot bojowy", 3, context, response);
    }

    @Test
    public void testExecute12() {
        command.execute("wez mlot bojowy 33", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot bojowy", 33, context, response);
    }

    @Test
    public void testExecute13() {
        command.execute("wez mlot\n", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot\n", 1, context, response);
    }

    @Test
    public void testExecute14() {
        command.execute("wez mlot bojowy\n", context, response);
        verify(playerLocationInteractionService).pickUpItems("mlot bojowy\n", 1, context, response);
    }
}