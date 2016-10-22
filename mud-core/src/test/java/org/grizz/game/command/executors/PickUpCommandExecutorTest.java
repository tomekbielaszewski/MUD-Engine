package org.grizz.game.command.executors;

import com.google.common.collect.Lists;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PickUpCommandExecutorTest {
    private static final String ITEM_NAME = "item";
    private static final String PICK_UP_EVENT = "pick up event";
    private static final String PICK_UP_EVENT_KEY = "event.player.picked.up.items";
    private static final String LOCATION_ID = "location id";
    @Mock
    private LocationService locationService;
    @Mock
    private EquipmentService equipmentService;
    @Mock
    private LocationRepo locationRepo;
    @Mock
    private EventService eventService;

    @InjectMocks
    private PickUpCommandExecutor commandExecutor = new PickUpCommandExecutor();

    @Test
    public void picksUpItemFromLocationAndPassesToEquipmentService() throws Exception {
        Player player = dummyPlayer();
        Location location = dummyLocation();
        PlayerResponse response = new PlayerResponse();
        List<Item> itemsToPickUp = Lists.newArrayList(dummyItem());
        when(locationService.removeItems(ITEM_NAME, 1, location)).thenReturn(itemsToPickUp);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);

        commandExecutor.pickUp(ITEM_NAME, 1, player, response);

        verify(locationService).removeItems(ITEM_NAME, 1, location);
        verify(equipmentService).addItems(itemsToPickUp, player, response);
    }

    @Test
    public void notifiesPlayerAboutPickedUpItems() throws Exception {
        Player player = dummyPlayer();
        Location location = dummyLocation();
        PlayerResponse response = new PlayerResponse();
        List<Item> itemsToPickUp = Lists.newArrayList(dummyItem());
        when(locationService.removeItems(ITEM_NAME, 1, location)).thenReturn(itemsToPickUp);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(eventService.getEvent(PICK_UP_EVENT_KEY)).thenReturn(PICK_UP_EVENT);

        commandExecutor.pickUp(ITEM_NAME, 1, player, response);

        verify(eventService).getEvent(PICK_UP_EVENT_KEY);
        assertThat(response.getPlayerEvents(), hasItem(PICK_UP_EVENT));
    }

    @Test(expected = InvalidAmountException.class)
    public void throwsExceptionWhenPickingUpZeroItems() throws Exception {
        commandExecutor.pickUp(ITEM_NAME, 0, dummyPlayer(), new PlayerResponse());
    }

    @Test(expected = InvalidAmountException.class)
    public void throwsExceptionWhenPickingUpNegativeNumberOfItems() throws Exception {
        commandExecutor.pickUp(ITEM_NAME, -1, dummyPlayer(), new PlayerResponse());
    }

    private Player dummyPlayer() {
        return Player.builder()
                .currentLocation(LOCATION_ID)
                .build();
    }

    private Location dummyLocation() {
        return Location.builder().build();
    }

    private Item dummyItem() {
        return Armor.builder().build();
    }
}