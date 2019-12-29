package org.grizz.game.command.executors.system;

import com.google.common.collect.Lists;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LookAroundCommandExecutorTest {
    private static final String LOCATION_ID = "id";
    private static final String PLAYER_NAME = "name1";
    private static final String PLAYER_NAME_2 = "name2";
    private static final String PLAYER_NAME_3 = "name3";
    private static final String SCRIPT_ID = "script";

    @Mock
    private LocationRepo locationRepo;
    @Mock
    private ScriptRepo scriptRepo;
    @Mock
    private ScriptRunner scriptRunner;
    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private LookAroundCommandExecutor executor = new LookAroundCommandExecutor();

    @Test
    public void addsCurrentLocationToPlayerResponse() throws Exception {
        when(locationRepo.get(LOCATION_ID)).thenReturn(Location.builder().id(LOCATION_ID).build());
        PlayerResponse response = new PlayerResponse();
        Player player = dummyPlayer(PLAYER_NAME, LOCATION_ID);

        executor.lookAround(player, response);

        assertThat(response.getCurrentLocation().getId(), is(player.getCurrentLocation()));
    }

    @Test
    public void findsOtherPlayersOnLocation() throws Exception {
        PlayerResponse response = new PlayerResponse();
        Player player = dummyPlayer(PLAYER_NAME, LOCATION_ID);
        Location location = Location.builder().id(LOCATION_ID).build();
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(playerRepository.findByCurrentLocation(LOCATION_ID)).thenReturn(Lists.newArrayList(
                player,
                dummyPlayer(PLAYER_NAME_2, LOCATION_ID),
                dummyPlayer(PLAYER_NAME_3, LOCATION_ID)
        ));

        executor.lookAround(player, response);

        assertThat(response.getPlayers(), hasSize(2));
        assertThat(response.getPlayers(), hasItem(PLAYER_NAME_2));
        assertThat(response.getPlayers(), hasItem(PLAYER_NAME_3));
        assertThat(response.getPlayers(), not(hasItem(PLAYER_NAME)));
        verify(playerRepository).findByCurrentLocation(LOCATION_ID);
    }

    @Test
    public void callsOnShowScriptWhenPresent() throws Exception {
        PlayerResponse response = new PlayerResponse();
        Player player = dummyPlayer(PLAYER_NAME, LOCATION_ID);
        Location location = Location.builder().id(LOCATION_ID).onShowScript(SCRIPT_ID).build();
        Script script = Script.builder().id(SCRIPT_ID).build();
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(scriptRepo.get(SCRIPT_ID)).thenReturn(script);

        executor.lookAround(player, response);

        verify(scriptRepo).get(SCRIPT_ID);
        verify(scriptRunner).execute(script, player, response);
    }

    @Test
    public void doesNotTryToRunOnShowScriptWhenAbsent() throws Exception {
        PlayerResponse response = new PlayerResponse();
        Player player = dummyPlayer(PLAYER_NAME, LOCATION_ID);
        Location location = Location.builder().id(LOCATION_ID).build();
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);

        executor.lookAround(player, response);

        verify(scriptRepo, never()).get(SCRIPT_ID);
        verify(scriptRunner, never()).execute(any(), eq(player), eq(response));
    }

    private Player dummyPlayer(String name, String location) {
        return Player.builder()
                .name(name)
                .currentLocation(location)
                .build();
    }
}
