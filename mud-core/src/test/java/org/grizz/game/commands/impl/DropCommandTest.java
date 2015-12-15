package org.grizz.game.commands.impl;

import com.google.common.collect.Maps;
import org.grizz.game.CommandTestUtils;
import org.grizz.game.TestContext;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.complex.PlayerLocationInteractionService;
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
public class DropCommandTest {
    @Autowired
    @InjectMocks
    private DropCommand command;

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

        commands.put("wyrzuc mlot", Boolean.TRUE);
        commands.put("wyrzuc mlot 3", Boolean.TRUE);
        commands.put("wyrzuc mlot bojowy", Boolean.TRUE);
        commands.put("wyrzuc mlot bojowy 3", Boolean.TRUE);
        commands.put("wyrzuc mlot bojowy 33", Boolean.TRUE);
        commands.put("wyrzuc mlot\n", Boolean.TRUE);
        commands.put("wyrzuc mlot bojowy\n", Boolean.TRUE);
        commands.put("wyrzuc \n", Boolean.TRUE);//this should never happen - command is trimmed before execution
        commands.put("wyrzuc mlot 3\n", Boolean.FALSE);
        commands.put("wyrzuc mlot bojowy 3\n", Boolean.FALSE);
        commands.put("wyrzuc ", Boolean.FALSE);
        commands.put("wyrzuc mlot bojowy 3 3", Boolean.FALSE);
        commands.put("wyrzuc mlot bojowy 3 3\n", Boolean.FALSE);
        commands.put("wyrzuc mlot bojowy 3 3 3\n", Boolean.FALSE);
        commands.put("wyrzuc mlot bojowy3\n", Boolean.FALSE);
        commands.put("wyrzuc mlot bojowy 3as\n", Boolean.FALSE);

        commands.put("porzuc mlot", Boolean.TRUE);
        commands.put("porzuc mlot 3", Boolean.TRUE);
        commands.put("porzuc mlot bojowy", Boolean.TRUE);
        commands.put("porzuc mlot bojowy 3", Boolean.TRUE);
        commands.put("porzuc mlot bojowy 33", Boolean.TRUE);
        commands.put("porzuc mlot\n", Boolean.TRUE);
        commands.put("porzuc mlot bojowy\n", Boolean.TRUE);
        commands.put("porzuc \n", Boolean.TRUE);//this should never happen - command is trimmed before execution
        commands.put("porzuc mlot 3\n", Boolean.FALSE);
        commands.put("porzuc mlot bojowy 3\n", Boolean.FALSE);
        commands.put("porzuc ", Boolean.FALSE);
        commands.put("porzuc mlot bojowy 3 3", Boolean.FALSE);
        commands.put("porzuc mlot bojowy 3 3\n", Boolean.FALSE);
        commands.put("porzuc mlot bojowy 3 3 3\n", Boolean.FALSE);
        commands.put("porzuc mlot bojowy3\n", Boolean.FALSE);
        commands.put("porzuc mlot bojowy 3as\n", Boolean.FALSE);

        CommandTestUtils.testCommands(commands, command::accept);
    }

    @Test
    public void testExecute() {
        command.execute("wyrzuc mlot", context, response);
        verify(playerLocationInteractionService).dropItems("mlot", 1, context, response);
    }

    @Test
    public void testExecute2() {
        command.execute("wyrzuc mlot 3", context, response);
        verify(playerLocationInteractionService).dropItems("mlot", 3, context, response);
    }

    @Test
    public void testExecute3() {
        command.execute("wyrzuc mlot bojowy", context, response);
        verify(playerLocationInteractionService).dropItems("mlot bojowy", 1, context, response);
    }

    @Test
    public void testExecute4() {
        command.execute("wyrzuc mlot bojowy 3", context, response);
        verify(playerLocationInteractionService).dropItems("mlot bojowy", 3, context, response);
    }

    @Test
    public void testExecute5() {
        command.execute("wyrzuc mlot bojowy 33", context, response);
        verify(playerLocationInteractionService).dropItems("mlot bojowy", 33, context, response);
    }

    @Test
    public void testExecute6() {
        command.execute("wyrzuc mlot\n", context, response);
        verify(playerLocationInteractionService).dropItems("mlot\n", 1, context, response);
    }

    @Test
    public void testExecute7() {
        command.execute("wyrzuc mlot bojowy\n", context, response);
        verify(playerLocationInteractionService).dropItems("mlot bojowy\n", 1, context, response);
    }

    @Test
    public void testExecute8() {
        command.execute("porzuc mlot", context, response);
        verify(playerLocationInteractionService).dropItems("mlot", 1, context, response);
    }

    @Test
    public void testExecute9() {
        command.execute("porzuc mlot 3", context, response);
        verify(playerLocationInteractionService).dropItems("mlot", 3, context, response);
    }

    @Test
    public void testExecute10() {
        command.execute("porzuc mlot bojowy", context, response);
        verify(playerLocationInteractionService).dropItems("mlot bojowy", 1, context, response);
    }

    @Test
    public void testExecute11() {
        command.execute("porzuc mlot bojowy 3", context, response);
        verify(playerLocationInteractionService).dropItems("mlot bojowy", 3, context, response);
    }

    @Test
    public void testExecute12() {
        command.execute("porzuc mlot bojowy 33", context, response);
        verify(playerLocationInteractionService).dropItems("mlot bojowy", 33, context, response);
    }

    @Test
    public void testExecute13() {
        command.execute("porzuc mlot\n", context, response);
        verify(playerLocationInteractionService).dropItems("mlot\n", 1, context, response);
    }

    @Test
    public void testExecute14() {
        command.execute("porzuc mlot bojowy\n", context, response);
        verify(playerLocationInteractionService).dropItems("mlot bojowy\n", 1, context, response);
    }
}