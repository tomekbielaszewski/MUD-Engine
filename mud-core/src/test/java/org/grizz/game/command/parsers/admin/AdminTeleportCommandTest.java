package org.grizz.game.command.parsers.admin;

import org.grizz.game.command.executors.admin.AdminTeleportCommandExecutor;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminTeleportCommandTest {
    private static final String DEFAULT_LOCATION_ID = "default location id";

    @Mock
    private Environment environment;
    @Mock
    private AdminTeleportCommandExecutor adminCommandExecutor;

    @InjectMocks
    private AdminTeleportCommand command = new AdminTeleportCommand(environment);

    @Before
    public void setUp() {
        when(environment.getProperty(command.getClass().getCanonicalName()))
                .thenReturn("przenies gracza (?<playerName>[\\w-]{4,}) na (?<locationId>[\\w-]+);" +
                        "teleportuj (?<playerName>[\\w-]{4,}) (?<locationId>[\\w-]+);" +
                        "teleportuj (?<playerName>[\\w-]{4,})");
    }

    @Test
    public void testAccept() throws Exception {
        assertTrue(command.accept("przenies gracza Grizz na 1"));
        assertTrue(command.accept("przenies gracza Grizz na xyz1"));
        assertTrue(command.accept("przenies gracza Grizz na 1xyz"));
        assertTrue(command.accept("przenies gracza Grizz na 1_xyz"));
        assertTrue(command.accept("przenies gracza Grizz-lol na 1"));
        assertTrue(command.accept("przenies gracza Grizz-lol na xyz1"));
        assertTrue(command.accept("przenies gracza Grizz-lol na 1xyz"));
        assertTrue(command.accept("przenies gracza Grizz-lol na 1_xyz"));
        assertTrue(command.accept("przenies gracza Grizz na lokacja-1"));
        assertTrue(command.accept("przenies gracza Grizz na lokacja-xyz1"));
        assertTrue(command.accept("przenies gracza Grizz na lokacja-1xyz"));
        assertTrue(command.accept("przenies gracza Grizz na lokacja-1_xyz"));
        assertTrue(command.accept("przenies gracza Grizz_lol na lokacja-1"));
        assertTrue(command.accept("przenies gracza Grizz_lol na lokacja-xyz1"));
        assertTrue(command.accept("przenies gracza Grizz_lol na lokacja-1xyz"));
        assertTrue(command.accept("przenies gracza Grizz_lol na lokacja-1_xyz"));
        assertTrue(command.accept("przenies gracza 12Grizz na 1"));
        assertTrue(command.accept("przenies gracza 12Grizz na xyz1"));
        assertTrue(command.accept("przenies gracza 12Grizz na 1xyz"));
        assertTrue(command.accept("przenies gracza 12Grizz na 1_xyz"));
        assertTrue(command.accept("przenies gracza 12Grizz-lol na 1"));
        assertTrue(command.accept("przenies gracza 12Grizz-lol na xyz1"));
        assertTrue(command.accept("przenies gracza 12Grizz-lol na 1xyz"));
        assertTrue(command.accept("przenies gracza 12Grizz-lol na 1_xyz"));
        assertTrue(command.accept("przenies gracza 12Grizz na lokacja-1"));
        assertTrue(command.accept("przenies gracza 12Grizz na lokacja-xyz1"));
        assertTrue(command.accept("przenies gracza 12Grizz na lokacja-1xyz"));
        assertTrue(command.accept("przenies gracza 12Grizz na lokacja-1_xyz"));
        assertTrue(command.accept("przenies gracza 12Grizz_lol na lokacja-1"));
        assertTrue(command.accept("przenies gracza 12Grizz_lol na lokacja-xyz1"));
        assertTrue(command.accept("przenies gracza 12Grizz_lol na lokacja-1xyz"));
        assertTrue(command.accept("przenies gracza 12Grizz_lol na lokacja-1_xyz"));

        assertTrue(command.accept("teleportuj Grizz 1"));
        assertTrue(command.accept("teleportuj Grizz xyz1"));
        assertTrue(command.accept("teleportuj Grizz 1xyz"));
        assertTrue(command.accept("teleportuj Grizz 1_xyz"));
        assertTrue(command.accept("teleportuj Grizz-lol 1"));
        assertTrue(command.accept("teleportuj Grizz-lol xyz1"));
        assertTrue(command.accept("teleportuj Grizz-lol 1xyz"));
        assertTrue(command.accept("teleportuj Grizz-lol 1_xyz"));
        assertTrue(command.accept("teleportuj Grizz lokacja-1"));
        assertTrue(command.accept("teleportuj Grizz lokacja-xyz1"));
        assertTrue(command.accept("teleportuj Grizz lokacja-1xyz"));
        assertTrue(command.accept("teleportuj Grizz lokacja-1_xyz"));
        assertTrue(command.accept("teleportuj Grizz_lol lokacja-1"));
        assertTrue(command.accept("teleportuj Grizz_lol lokacja-xyz1"));
        assertTrue(command.accept("teleportuj Grizz_lol lokacja-1xyz"));
        assertTrue(command.accept("teleportuj Grizz_lol lokacja-1_xyz"));
        assertTrue(command.accept("teleportuj 12Grizz 1"));
        assertTrue(command.accept("teleportuj 12Grizz xyz1"));
        assertTrue(command.accept("teleportuj 12Grizz 1xyz"));
        assertTrue(command.accept("teleportuj 12Grizz 1_xyz"));
        assertTrue(command.accept("teleportuj 12Grizz-lol 1"));
        assertTrue(command.accept("teleportuj 12Grizz-lol xyz1"));
        assertTrue(command.accept("teleportuj 12Grizz-lol 1xyz"));
        assertTrue(command.accept("teleportuj 12Grizz-lol 1_xyz"));
        assertTrue(command.accept("teleportuj 12Grizz lokacja-1"));
        assertTrue(command.accept("teleportuj 12Grizz lokacja-xyz1"));
        assertTrue(command.accept("teleportuj 12Grizz lokacja-1xyz"));
        assertTrue(command.accept("teleportuj 12Grizz lokacja-1_xyz"));
        assertTrue(command.accept("teleportuj 12Grizz_lol lokacja-1"));
        assertTrue(command.accept("teleportuj 12Grizz_lol lokacja-xyz1"));
        assertTrue(command.accept("teleportuj 12Grizz_lol lokacja-1xyz"));
        assertTrue(command.accept("teleportuj 12Grizz_lol lokacja-1_xyz"));

        assertFalse(command.accept("przenies gracza  na 1"));
        assertFalse(command.accept("przenies gracza Grizz na "));
        assertFalse(command.accept("przenies gracza na 1xyz"));
        assertFalse(command.accept("przenies gracza Grizz na"));
        assertFalse(command.accept(""));
    }

    @Test
    public void testExecute() throws Exception {
        verifyPassedVariables("przenies gracza Grizz na 1", "Grizz", "1");
        verifyPassedVariables("przenies gracza Grizz na xyz1", "Grizz", "xyz1");
        verifyPassedVariables("przenies gracza Grizz na 1xyz", "Grizz", "1xyz");
        verifyPassedVariables("przenies gracza Grizz na 1_xyz", "Grizz", "1_xyz");
        verifyPassedVariables("przenies gracza Grizz na lokacja-1", "Grizz", "lokacja-1");
        verifyPassedVariables("przenies gracza Grizz na lokacja-xyz1", "Grizz", "lokacja-xyz1");
        verifyPassedVariables("przenies gracza Grizz na lokacja-1xyz", "Grizz", "lokacja-1xyz");
        verifyPassedVariables("przenies gracza Grizz na lokacja-1_xyz", "Grizz", "lokacja-1_xyz");

        verifyPassedVariables("teleportuj Grizz 1", "Grizz", "1");
        verifyPassedVariables("teleportuj Grizz xyz1", "Grizz", "xyz1");
        verifyPassedVariables("teleportuj Grizz 1xyz", "Grizz", "1xyz");
        verifyPassedVariables("teleportuj Grizz 1_xyz", "Grizz", "1_xyz");
        verifyPassedVariables("teleportuj Grizz lokacja-1", "Grizz", "lokacja-1");
        verifyPassedVariables("teleportuj Grizz lokacja-xyz1", "Grizz", "lokacja-xyz1");
        verifyPassedVariables("teleportuj Grizz lokacja-1xyz", "Grizz", "lokacja-1xyz");
        verifyPassedVariables("teleportuj Grizz lokacja-1_xyz", "Grizz", "lokacja-1_xyz");

        verifyPassedVariables("teleportuj Grizz", "Grizz", DEFAULT_LOCATION_ID);
        verifyPassedVariables("teleportuj Grizz-lol", "Grizz-lol", DEFAULT_LOCATION_ID);
        verifyPassedVariables("teleportuj 12Grizz", "12Grizz", DEFAULT_LOCATION_ID);
        verifyPassedVariables("teleportuj 12Grizz_lol", "12Grizz_lol", DEFAULT_LOCATION_ID);
    }

    private void verifyPassedVariables(String userCommand, String expectedPlayerName, String expectedLocationId) {
        Player player = Player.builder().currentLocation(DEFAULT_LOCATION_ID).build();
        PlayerResponse response = new PlayerResponse();

        command.execute(userCommand, player, response);
        verify(adminCommandExecutor).teleport(expectedPlayerName, expectedLocationId, player, response);
        verifyNoMoreInteractions(adminCommandExecutor);
    }
}