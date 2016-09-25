package org.grizz.game.command.executors.system;

import org.grizz.game.exception.CantGoThereException;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.EventService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.grizz.game.model.Direction.SOUTH;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoveCommandExecutorTest {
    private static final String PLAYER_NAME = "player";
    private static final String SOURCE_LOCATION_ID = "source-location-id";
    private static final String TARGET_LOCATION_ID = "target-location-id";
    @Mock
    private LocationRepo locationRepo;
    @Mock
    private Environment env;
    @Mock
    private LookAroundCommandExecutor lookAroundCommandExecutor;
    @Mock
    private EventService eventService;
    @Mock
    private MultiplayerNotificationService notificationService;

    @InjectMocks
    private MoveCommandExecutor commandExecutor = new MoveCommandExecutor();

    @Test(expected = CantGoThereException.class)
    public void throwsCantGoThereExceptionWhenMovementNotPossible() throws Exception {
        Player player = dummyPlayer(PLAYER_NAME, SOURCE_LOCATION_ID);
        when(locationRepo.get(SOURCE_LOCATION_ID)).thenReturn(Location.builder().id(SOURCE_LOCATION_ID).south(null).build());

        commandExecutor.move(SOUTH, player, new PlayerResponse());
    }

    @Test
    public void setsNewCurrentPositionAndCallsLookAroundCommand() throws Exception {
        PlayerResponse response = new PlayerResponse();
        Player player = dummyPlayer(PLAYER_NAME, SOURCE_LOCATION_ID);
        when(locationRepo.get(SOURCE_LOCATION_ID)).thenReturn(Location.builder().id(SOURCE_LOCATION_ID).south(TARGET_LOCATION_ID).build());
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(Location.builder().id(TARGET_LOCATION_ID).build());

        commandExecutor.move(SOUTH, player, response);

        assertThat(player.getCurrentLocation(), is(TARGET_LOCATION_ID));
        assertThat(player.getPastLocation(), is(SOURCE_LOCATION_ID));
        verify(lookAroundCommandExecutor).lookAround(player, response);
    }

    @Test
    public void broadcastsMovementEventOnLocations() throws Exception {
        String sourceLocationEvent = "source location event";
        String targetLocationEvent = "target location event";
        PlayerResponse response = new PlayerResponse();
        Player player = dummyPlayer(PLAYER_NAME, SOURCE_LOCATION_ID);
        Location sourceLocation = Location.builder().id(SOURCE_LOCATION_ID).south(TARGET_LOCATION_ID).build();
        Location targetLocation = Location.builder().id(TARGET_LOCATION_ID).build();
        when(locationRepo.get(SOURCE_LOCATION_ID)).thenReturn(sourceLocation);
        when(locationRepo.get(TARGET_LOCATION_ID)).thenReturn(targetLocation);
        when(eventService.getEvent("multiplayer.event.player.left.location.south", PLAYER_NAME)).thenReturn(sourceLocationEvent);
        when(eventService.getEvent("multiplayer.event.player.entered.location", PLAYER_NAME)).thenReturn(targetLocationEvent);

        commandExecutor.move(SOUTH, player, response);

        verify(notificationService).broadcast(sourceLocation, sourceLocationEvent, player);
        verify(notificationService).broadcast(targetLocation, targetLocationEvent, player);
    }

    @Test
    public void throwsExceptionWhenScriptDoesNotAllowLeaveLocation() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void throwsExceptionWhenScriptDoesNotEnterNextLocation() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void callsOnLeaveScript() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void callsOnEnterScript() throws Exception {
        throw new NotImplementedException();
    }

    private Player dummyPlayer(String name, String currentLocation) {
        return Player.builder().name(name).currentLocation(currentLocation).build();
    }
}