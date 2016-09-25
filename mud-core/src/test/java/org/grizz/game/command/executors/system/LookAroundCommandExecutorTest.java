package org.grizz.game.command.executors.system;

import com.google.common.collect.Lists;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LookAroundCommandExecutorTest {
    private static final String LOCATION_ID = "id";
    private static final String PLAYER_ID = "id1";
    private static final String PLAYER_ID_2 = "id2";
    private static final String PLAYER_ID_3 = "id3";
    private static final String PLAYER_NAME = "name1";
    private static final String PLAYER_NAME_2 = "name2";
    private static final String PLAYER_NAME_3 = "name3";

    @Mock
    private LocationRepo locationRepo;
    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private LookAroundCommandExecutor executor = new LookAroundCommandExecutor();

    @Test
    public void addsCurrentLocationToPlayerResponse() throws Exception {
        when(locationRepo.get(LOCATION_ID)).thenReturn(Location.builder().id(LOCATION_ID).build());
        PlayerResponse response = new PlayerResponse();
        Player player = dummyPlayer(PLAYER_ID, PLAYER_NAME, LOCATION_ID);

        executor.lookAround(player, response);

        assertThat(response.getCurrentLocation().getId(), is(player.getCurrentLocation()));
    }

    @Test
    public void findsOtherPlayersOnLocation() throws Exception {
        PlayerResponse response = new PlayerResponse();
        Player player = dummyPlayer(PLAYER_ID, PLAYER_NAME, LOCATION_ID);
        when(playerRepository.findByCurrentLocation(LOCATION_ID)).thenReturn(Lists.newArrayList(
                player,
                dummyPlayer(PLAYER_ID_2, PLAYER_NAME_2, LOCATION_ID),
                dummyPlayer(PLAYER_ID_3, PLAYER_NAME_3, LOCATION_ID)
        ));

        executor.lookAround(player, response);

        assertThat(response.getPlayers(), hasSize(2));
        assertThat(response.getPlayers(), hasItem(PLAYER_NAME_2));
        assertThat(response.getPlayers(), hasItem(PLAYER_NAME_3));
        assertThat(response.getPlayers(), not(hasItem(PLAYER_NAME)));
        verify(playerRepository).findByCurrentLocation(LOCATION_ID);
    }

    @Test
    public void callsOnShowScript() throws Exception {
        throw new NotImplementedException();
    }

    private Player dummyPlayer(String id, String name, String location) {
        return Player.builder()
                .id(id)
                .name(name)
                .currentLocation(location)
                .build();
    }
}