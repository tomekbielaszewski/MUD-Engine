package org.grizz.game.commands.impl.admin;

import com.google.common.collect.Maps;
import org.grizz.game.CommandTestUtils;
import org.grizz.game.TestContext;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.service.complex.AdministratorService;
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
import static org.mockito.Mockito.when;

/**
 * Created by Grizz on 2015-12-04.
 */
@ContextConfiguration(classes = {TestContext.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AdminTeleportCommandTest {
    @Autowired
    @InjectMocks
    private AdminTeleportCommand command;

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
    public void testAccept() throws Exception {
        Map<String, Boolean> commands = Maps.newHashMap();

        commands.put("przenies gracza Grizz na 1", Boolean.TRUE);
        commands.put("przenies gracza Grizz na xyz1", Boolean.TRUE);
        commands.put("przenies gracza Grizz na 1xyz", Boolean.TRUE);
        commands.put("przenies gracza Grizz na 1_xyz", Boolean.TRUE);
        commands.put("przenies gracza Grizz-lol na 1", Boolean.TRUE);
        commands.put("przenies gracza Grizz-lol na xyz1", Boolean.TRUE);
        commands.put("przenies gracza Grizz-lol na 1xyz", Boolean.TRUE);
        commands.put("przenies gracza Grizz-lol na 1_xyz", Boolean.TRUE);
        commands.put("przenies gracza Grizz na lokacja-1", Boolean.TRUE);
        commands.put("przenies gracza Grizz na lokacja-xyz1", Boolean.TRUE);
        commands.put("przenies gracza Grizz na lokacja-1xyz", Boolean.TRUE);
        commands.put("przenies gracza Grizz na lokacja-1_xyz", Boolean.TRUE);
        commands.put("przenies gracza Grizz_lol na lokacja-1", Boolean.TRUE);
        commands.put("przenies gracza Grizz_lol na lokacja-xyz1", Boolean.TRUE);
        commands.put("przenies gracza Grizz_lol na lokacja-1xyz", Boolean.TRUE);
        commands.put("przenies gracza Grizz_lol na lokacja-1_xyz", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz na 1", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz na xyz1", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz na 1xyz", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz na 1_xyz", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz-lol na 1", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz-lol na xyz1", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz-lol na 1xyz", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz-lol na 1_xyz", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz na lokacja-1", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz na lokacja-xyz1", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz na lokacja-1xyz", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz na lokacja-1_xyz", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz_lol na lokacja-1", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz_lol na lokacja-xyz1", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz_lol na lokacja-1xyz", Boolean.TRUE);
        commands.put("przenies gracza 12Grizz_lol na lokacja-1_xyz", Boolean.TRUE);

        commands.put("teleportuj Grizz 1", Boolean.TRUE);
        commands.put("teleportuj Grizz xyz1", Boolean.TRUE);
        commands.put("teleportuj Grizz 1xyz", Boolean.TRUE);
        commands.put("teleportuj Grizz 1_xyz", Boolean.TRUE);
        commands.put("teleportuj Grizz-lol 1", Boolean.TRUE);
        commands.put("teleportuj Grizz-lol xyz1", Boolean.TRUE);
        commands.put("teleportuj Grizz-lol 1xyz", Boolean.TRUE);
        commands.put("teleportuj Grizz-lol 1_xyz", Boolean.TRUE);
        commands.put("teleportuj Grizz lokacja-1", Boolean.TRUE);
        commands.put("teleportuj Grizz lokacja-xyz1", Boolean.TRUE);
        commands.put("teleportuj Grizz lokacja-1xyz", Boolean.TRUE);
        commands.put("teleportuj Grizz lokacja-1_xyz", Boolean.TRUE);
        commands.put("teleportuj Grizz_lol lokacja-1", Boolean.TRUE);
        commands.put("teleportuj Grizz_lol lokacja-xyz1", Boolean.TRUE);
        commands.put("teleportuj Grizz_lol lokacja-1xyz", Boolean.TRUE);
        commands.put("teleportuj Grizz_lol lokacja-1_xyz", Boolean.TRUE);
        commands.put("teleportuj 12Grizz 1", Boolean.TRUE);
        commands.put("teleportuj 12Grizz xyz1", Boolean.TRUE);
        commands.put("teleportuj 12Grizz 1xyz", Boolean.TRUE);
        commands.put("teleportuj 12Grizz 1_xyz", Boolean.TRUE);
        commands.put("teleportuj 12Grizz-lol 1", Boolean.TRUE);
        commands.put("teleportuj 12Grizz-lol xyz1", Boolean.TRUE);
        commands.put("teleportuj 12Grizz-lol 1xyz", Boolean.TRUE);
        commands.put("teleportuj 12Grizz-lol 1_xyz", Boolean.TRUE);
        commands.put("teleportuj 12Grizz lokacja-1", Boolean.TRUE);
        commands.put("teleportuj 12Grizz lokacja-xyz1", Boolean.TRUE);
        commands.put("teleportuj 12Grizz lokacja-1xyz", Boolean.TRUE);
        commands.put("teleportuj 12Grizz lokacja-1_xyz", Boolean.TRUE);
        commands.put("teleportuj 12Grizz_lol lokacja-1", Boolean.TRUE);
        commands.put("teleportuj 12Grizz_lol lokacja-xyz1", Boolean.TRUE);
        commands.put("teleportuj 12Grizz_lol lokacja-1xyz", Boolean.TRUE);
        commands.put("teleportuj 12Grizz_lol lokacja-1_xyz", Boolean.TRUE);

        commands.put("przenies gracza  na 1", Boolean.FALSE);
        commands.put("przenies gracza Grizz na ", Boolean.FALSE);
        commands.put("przenies gracza na 1xyz", Boolean.FALSE);
        commands.put("przenies gracza Grizz na", Boolean.FALSE);
        commands.put("", Boolean.FALSE);

        CommandTestUtils.testCommands(commands, command::accept);
    }

    @Test
    public void testExecute() throws Exception {
        command.execute("teleportuj Grizz-lol 1_xyz", context, response);
        verify(administratorService).teleport("Grizz-lol", "1_xyz", context, response);
    }

    @Test
    public void testExecute2() throws Exception {
        command.execute("teleportuj Grizz-lol 1", context, response);
        verify(administratorService).teleport("Grizz-lol", "1", context, response);
    }

    @Test
    public void testExecute3() throws Exception {
        command.execute("teleportuj 12Grizz_lol 1", context, response);
        verify(administratorService).teleport("12Grizz_lol", "1", context, response);
    }

    @Test
    public void testExecute4() throws Exception {
        command.execute("przenies gracza Grizz-lol na 1_xyz", context, response);
        verify(administratorService).teleport("Grizz-lol", "1_xyz", context, response);
    }

    @Test
    public void testExecute5() throws Exception {
        command.execute("przenies gracza Grizz-lol na 1", context, response);
        verify(administratorService).teleport("Grizz-lol", "1", context, response);
    }

    @Test
    public void testExecute6() throws Exception {
        command.execute("przenies gracza 12Grizz_lol na 1", context, response);
        verify(administratorService).teleport("12Grizz_lol", "1", context, response);
    }

    @Test
    public void testExecute10() throws Exception {
        when(context.getCurrentLocation()).thenReturn("1");
        command.execute("teleportuj Grizz", context, response);
        verify(administratorService).teleport("Grizz", "1", context, response);
    }
}