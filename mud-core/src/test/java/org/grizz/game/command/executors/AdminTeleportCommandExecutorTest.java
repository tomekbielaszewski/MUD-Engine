package org.grizz.game.command.executors;

import org.grizz.game.command.executors.admin.AdminTeleportCommandExecutor;
import org.grizz.game.command.executors.system.LookAroundCommandExecutor;
import org.grizz.game.exception.PlayerDoesNotExistException;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.AdminRightsService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminTeleportCommandExecutorTest {
    private static final String PLAYER_NAME = "player name";
    private static final String ADMIN_NAME = "admin name";
    private static final String SOURCE_LOCATION_ID = "source-location";
    private static final String TARGET_LOCATION_ID = "target-location";

    @Mock
    private AdminRightsService adminRightsService;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private LocationRepo locationRepo;
    @Mock
    private EventService eventService;
    @Mock
    private MultiplayerNotificationService notificationService;
    @Mock
    private LookAroundCommandExecutor lookAroundCommandExecutor;

    @InjectMocks
    private AdminTeleportCommandExecutor commandExecutor = new AdminTeleportCommandExecutor();

    @Test
    public void notifiesSourceLocationPlayers() throws Exception {
        Player player = dummyPlayer(PLAYER_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        Location sourceLocation = dummyLocation(SOURCE_LOCATION_ID);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);
        when(locationRepo.get(SOURCE_LOCATION_ID)).thenReturn(sourceLocation);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);
        String sourceLocationEvent = "source location event";
        when(eventService.getEvent("admin.command.player.teleportation.notification.broadcast.out", PLAYER_NAME)).thenReturn(sourceLocationEvent);

        commandExecutor.teleport(PLAYER_NAME, TARGET_LOCATION_ID, admin, new PlayerResponse());

        verify(eventService).getEvent("admin.command.player.teleportation.notification.broadcast.out", PLAYER_NAME);
        verify(notificationService).broadcast(sourceLocation, sourceLocationEvent, player);
    }

    @Test
    public void notifiesTargetLocationPlayers() throws Exception {
        Player player = dummyPlayer(PLAYER_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);
        String targetLocationEvent = "target location event";
        when(eventService.getEvent("admin.command.player.teleportation.notification.broadcast.in", PLAYER_NAME)).thenReturn(targetLocationEvent);

        commandExecutor.teleport(PLAYER_NAME, TARGET_LOCATION_ID, admin, new PlayerResponse());

        verify(eventService).getEvent("admin.command.player.teleportation.notification.broadcast.in", PLAYER_NAME);
        verify(notificationService).broadcast(targetLocation, targetLocationEvent, player);
    }

    @Test
    public void notifiesAdmin() throws Exception {
        Player player = dummyPlayer(PLAYER_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        PlayerResponse response = new PlayerResponse();
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);
        String adminEvent = "admin event";
        when(eventService.getEvent("admin.command.success.teleportation.notification", PLAYER_NAME, targetLocation.getName(),
                targetLocation.getId())).thenReturn(adminEvent);

        commandExecutor.teleport(PLAYER_NAME, TARGET_LOCATION_ID, admin, response);

        verify(eventService).getEvent("admin.command.success.teleportation.notification", PLAYER_NAME, targetLocation.getName(), targetLocation.getId());
        assertThat(response.getPlayerEvents(), hasSize(1));
        assertThat(response.getPlayerEvents(), hasItem(adminEvent));
    }

    @Test
    public void notifiesPlayer() throws Exception {
        Player player = dummyPlayer(PLAYER_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        String playerEvent = "player event";
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);
        when(eventService.getEvent("admin.command.player.teleportation.notification", ADMIN_NAME, targetLocation.getName())).thenReturn(playerEvent);
        PlayerResponse teleportedPlayerResponse = new PlayerResponse();
        teleportedPlayerResponse.getPlayerEvents().add(playerEvent);

        commandExecutor.teleport(PLAYER_NAME, TARGET_LOCATION_ID, admin, new PlayerResponse());

        verify(eventService).getEvent("admin.command.player.teleportation.notification", ADMIN_NAME, targetLocation.getName());
        verify(notificationService).send(player, teleportedPlayerResponse);
    }

    @Test
    public void teleportsPlayer() throws Exception {
        Player player = dummyPlayer(PLAYER_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);

        commandExecutor.teleport(PLAYER_NAME, TARGET_LOCATION_ID, admin, new PlayerResponse());

        assertThat(player.getPastLocation(), is(SOURCE_LOCATION_ID));
        assertThat(player.getCurrentLocation(), is(TARGET_LOCATION_ID));
        assertThat(admin.getCurrentLocation(), is(SOURCE_LOCATION_ID));
    }

    @Test
    public void teleportsAdmin() throws Exception {
        Player admin = dummyPlayer(ADMIN_NAME);
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);

        commandExecutor.teleport(ADMIN_NAME, TARGET_LOCATION_ID, admin, new PlayerResponse());

        assertThat(admin.getPastLocation(), is(SOURCE_LOCATION_ID));
        assertThat(admin.getCurrentLocation(), is(TARGET_LOCATION_ID));
    }

    @Test
    public void showsLocationToTheAdminWhenSelfTeleporting() throws Exception {
        Player admin = dummyPlayer(ADMIN_NAME);
        PlayerResponse response = new PlayerResponse();
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);

        commandExecutor.teleport(ADMIN_NAME, TARGET_LOCATION_ID, admin, response);

        verify(lookAroundCommandExecutor).lookAround(admin, response);
    }

    @Test
    public void showsLocationToThePlayer() throws Exception {
        Player player = dummyPlayer(PLAYER_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        String playerEvent = "player event";
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        when(eventService.getEvent("admin.command.player.teleportation.notification", ADMIN_NAME, targetLocation.getName())).thenReturn(playerEvent);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);
        PlayerResponse teleportedPlayerResponse = new PlayerResponse();
        teleportedPlayerResponse.getPlayerEvents().add(playerEvent);

        commandExecutor.teleport(PLAYER_NAME, TARGET_LOCATION_ID, admin, new PlayerResponse());

        verify(lookAroundCommandExecutor).lookAround(player, teleportedPlayerResponse);
    }

    @Test(expected = PlayerDoesNotExistException.class)
    public void throwsExceptionWhenRequestedPlayerDoesNotExist() throws Exception {
        Player admin = dummyPlayer(ADMIN_NAME);
        commandExecutor.teleport(PLAYER_NAME, TARGET_LOCATION_ID, admin, new PlayerResponse());
    }

    @Test
    public void savesRequestedPlayer() throws Exception {
        Player player = dummyPlayer(PLAYER_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);

        commandExecutor.teleport(PLAYER_NAME, TARGET_LOCATION_ID, admin, new PlayerResponse());

        verify(playerRepository).save(player);
    }

    @Test
    public void checksAdminRights() throws Exception {
        Player admin = dummyPlayer(ADMIN_NAME);
        Location targetLocation = dummyLocation(TARGET_LOCATION_ID);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);

        commandExecutor.teleport(ADMIN_NAME, TARGET_LOCATION_ID, admin, new PlayerResponse());

        verify(adminRightsService).checkAdminRights(admin);
    }

    private Player dummyPlayer(String playerName) {
        return Player.builder().name(playerName).currentLocation(SOURCE_LOCATION_ID).build();
    }

    private Location dummyLocation(String sourceLocationId) {
        return Location.builder().id(sourceLocationId).name("name" + sourceLocationId).build();
    }
}