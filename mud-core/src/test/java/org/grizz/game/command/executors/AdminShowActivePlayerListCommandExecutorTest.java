package org.grizz.game.command.executors;

import com.google.common.collect.Lists;
import org.grizz.game.command.executors.admin.AdminShowActivePlayerListCommandExecutor;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.AdminRightsService;
import org.grizz.game.service.EventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminShowActivePlayerListCommandExecutorTest {
    private static final String LOCATION_ID = "location";
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private EventService eventService;
    @Mock
    private LocationRepo locationRepo;
    @Mock
    private AdminRightsService adminRightsService;

    @InjectMocks
    private AdminShowActivePlayerListCommandExecutor commandExecutor = new AdminShowActivePlayerListCommandExecutor();

    @Test
    public void appendsHeaderOnlyOnce() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();

        commandExecutor.showPlayerList(1, admin, response);

        verify(eventService, times(1)).getEvent("admin.command.player.list.title");
    }

    @Test
    public void listsSameAmountOfPlayersAsFetchedFromDB() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        ArrayList<Player> players = Lists.newArrayList(dummyPlayer(), dummyPlayer(), dummyPlayer());
        when(playerRepository.findByLastActivityTimestampGreaterThan(anyLong())).thenReturn(players);
        when(locationRepo.get(LOCATION_ID)).thenReturn(dummyLocation());

        commandExecutor.showPlayerList(1, admin, response);

        verify(eventService, times(players.size())).getEvent(eq("admin.command.player.list.row"),
                any(), any(), eq("dummy location"), any());
    }

    @Test
    public void checksAdminRights() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();

        commandExecutor.showPlayerList(1, admin, response);

        verify(adminRightsService).checkAdminRights(admin);
    }

    private Player dummyPlayer() {
        return Player.builder().currentLocation(LOCATION_ID).build();
    }

    private Location dummyLocation() {
        return Location.builder().name("dummy location").build();
    }
}