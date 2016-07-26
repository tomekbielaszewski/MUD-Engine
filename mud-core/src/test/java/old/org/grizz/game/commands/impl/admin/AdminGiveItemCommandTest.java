package old.org.grizz.game.commands.impl.admin;

import com.google.common.collect.Maps;
import old.org.grizz.game.CommandTestUtils;
import old.org.grizz.game.TestContext;
import old.org.grizz.game.model.PlayerContext;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.service.complex.AdministratorService;
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
 * Created by Grizz on 2015-12-03.
 */
@ContextConfiguration(classes = {TestContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AdminGiveItemCommandTest {
    @Autowired
    @InjectMocks
    private AdminGiveItemCommand command;

    @Mock
    private AdministratorService administratorService;

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
        Map<String, Boolean> commands = Maps.newHashMap();

        commands.put("daj mlot bojowy graczowi Grizz", Boolean.TRUE);
        commands.put("daj 2 mlot bojowy graczowi Grizz", Boolean.TRUE);
        commands.put("daj mlot bojowy graczowi Grizz-lol", Boolean.TRUE);
        commands.put("daj 2 mlot bojowy graczowi Grizz-lol", Boolean.TRUE);
        commands.put("daj 22 mlot bojowy graczowi Grizz-lol", Boolean.TRUE);
        commands.put("daj mlot bojowy graczowi Grizz_lol", Boolean.TRUE);
        commands.put("daj 2 mlot bojowy graczowi Grizz_lol", Boolean.TRUE);
        commands.put("daj 22 mlot bojowy graczowi Grizz_lol", Boolean.TRUE);

        commands.put("daj mlot bojowy graczowi \n", Boolean.FALSE);
        commands.put("daj 2 mlot bojowy graczowi \n", Boolean.FALSE);
        commands.put("daj  graczowi Grizz", Boolean.FALSE);
        commands.put("daj 2  graczowi Grizz", Boolean.FALSE);
        commands.put("daj graczowi Grizz\n", Boolean.FALSE);
        commands.put("daj 2 graczowi Grizz\n", Boolean.FALSE);
        commands.put("daj mlot bojowy graczowi ", Boolean.FALSE);
        commands.put("daj 2 mlot bojowy graczowi ", Boolean.FALSE);
        commands.put("daj 22 2 mlot bojowy graczowi Grizz lol\n", Boolean.FALSE);

        CommandTestUtils.testCommands(commands, command::accept);
    }

    @Test
    public void testExecute() {
        command.execute("daj mlot bojowy graczowi Grizz", context, response);
        verify(administratorService).give("Grizz", "mlot bojowy", 1, context, response);
    }

    @Test
    public void testExecute2() {
        command.execute("daj 2 mlot bojowy graczowi Grizz", context, response);
        verify(administratorService).give("Grizz", "mlot bojowy", 2, context, response);
    }

    @Test
    public void testExecute3() {
        command.execute("daj mlot bojowy graczowi Grizz-lol", context, response);
        verify(administratorService).give("Grizz-lol", "mlot bojowy", 1, context, response);
    }

    @Test
    public void testExecute4() {
        command.execute("daj 2 mlot bojowy graczowi Grizz-lol", context, response);
        verify(administratorService).give("Grizz-lol", "mlot bojowy", 2, context, response);
    }

    @Test
    public void testExecute5() {
        command.execute("daj 22 mlot bojowy graczowi Grizz-lol", context, response);
        verify(administratorService).give("Grizz-lol", "mlot bojowy", 22, context, response);
    }

    @Test
    public void testExecute6() {
        command.execute("daj mlot bojowy graczowi Grizz_lol", context, response);
        verify(administratorService).give("Grizz_lol", "mlot bojowy", 1, context, response);
    }

    @Test
    public void testExecute7() {
        command.execute("daj 2 mlot bojowy graczowi Grizz_lol", context, response);
        verify(administratorService).give("Grizz_lol", "mlot bojowy", 2, context, response);
    }

    @Test
    public void testExecute8() {
        command.execute("daj 22 mlot bojowy graczowi Grizz_lol", context, response);
        verify(administratorService).give("Grizz_lol", "mlot bojowy", 22, context, response);
    }
}