package org.grizz.game.service.notifier;

import com.google.common.collect.Lists;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MultiplayerNotificationServiceTest {
    private static final String PLAYER_NAME_1 = "playerName1";
    private static final String PLAYER_NAME_2 = "playerName2";
    private static final String PLAYER_NAME_3 = "playerName3";
    private static final String LOCATION_ID = "location id";

    @Mock
    private Notifier notifier;
    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private MultiplayerNotificationService notificationService = new MultiplayerNotificationService();

    @Test
    public void broadcastsToAllPlayersExceptSender() throws Exception {
        String event = "event";
        Location location = Location.builder().id(LOCATION_ID).build();
        Player player = dummyPlayer(PLAYER_NAME_1);
        PlayerResponse response = new PlayerResponse();
        response.getPlayerEvents().add(event);
        when(playerRepository.findByCurrentLocation(LOCATION_ID)).thenReturn(Lists.newArrayList(
                player,
                dummyPlayer(PLAYER_NAME_2),
                dummyPlayer(PLAYER_NAME_3)
        ));

        notificationService.broadcast(location, event, player);

        verify(notifier).notify(PLAYER_NAME_2, response);
        verify(notifier).notify(PLAYER_NAME_3, response);
        verify(notifier, never()).notify(PLAYER_NAME_1, response);
    }

    @Test
    public void sendsSingleEvent() throws Exception {
        String event = "event";
        Player player = dummyPlayer(PLAYER_NAME_1);
        PlayerResponse response = new PlayerResponse();
        response.getPlayerEvents().add(event);

        notificationService.send(player, event);

        verify(notifier).notify(PLAYER_NAME_1, response);
    }

    @Test
    public void sendsWholePlayerResponse() throws Exception {
        String event = "event";
        String event2 = "event2";
        Player player = dummyPlayer(PLAYER_NAME_1);
        PlayerResponse response = new PlayerResponse();
        response.getPlayerEvents().add(event);
        response.getPlayerEvents().add(event2);
        response.setCurrentLocation(Location.builder().build());

        notificationService.send(player, response);

        verify(notifier).notify(PLAYER_NAME_1, response);
    }

    private Player dummyPlayer(String name) {
        return Player.builder().name(name).build();
    }
}