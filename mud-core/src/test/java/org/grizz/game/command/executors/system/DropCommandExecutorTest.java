package org.grizz.game.command.executors.system;

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
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DropCommandExecutorTest {
    private static final String ITEM_NAME = "item name";
    private static final String LOCATION_ID = "location id";
    private static final String MULTIPLAYER_DROP_EVENT_KEY = "multiplayer.event.player.drop.items";
    private static final String PLAYER_NAME = "player name";
    private static final String MULTIPLAYER_DROP_EVENT = "multiplayer drop event";
    private static final String DROP_EVENT_KEY = "event.player.drop.items";
    private static final String DROP_EVENT = "drop event";

    @Mock
    private LocationRepo locationRepo;
    @Mock
    private LocationService locationService;
    @Mock
    private EquipmentService equipmentService;
    @Mock
    private EventService eventService;
    @Mock
    private MultiplayerNotificationService notificationService;

    @InjectMocks
    private DropCommandExecutor commandExecutor = new DropCommandExecutor();

    @Test
    public void removesItemsFromEquipmentAndPutsThemOnLocation() throws Exception {
        int amount = 2;
        Player player = dummyPlayer();
        List<Item> itemsFromEquipment = Lists.newArrayList(dummyItem());
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(equipmentService.removeItems(ITEM_NAME, amount, player, response)).thenReturn(itemsFromEquipment);

        commandExecutor.drop(ITEM_NAME, amount, player, response);

        verify(equipmentService).removeItems(ITEM_NAME, amount, player, response);
        verify(locationService).addItems(itemsFromEquipment, location);
    }

    @Test
    public void name() throws Exception {
        int amount = 2;
        Player player = dummyPlayer();
        List<Item> itemsFromEquipment = Lists.newArrayList();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(equipmentService.removeItems(ITEM_NAME, amount, player, response)).thenReturn(itemsFromEquipment);

        commandExecutor.drop(ITEM_NAME, amount, player, response);

        verify(equipmentService).removeItems(ITEM_NAME, amount, player, response);
        verify(locationService, never()).addItems(itemsFromEquipment, location);
        assertThat(response.getPlayerEvents(), hasSize(0));
    }

    @Test
    public void notifiesOtherPlayersOnLocation() throws Exception {
        int amount = 2;
        String amountStr = "2";
        Player player = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        List<Item> itemsFromEquipment = Lists.newArrayList(dummyItem());
        when(eventService.getEvent(MULTIPLAYER_DROP_EVENT_KEY, PLAYER_NAME, amountStr, ITEM_NAME)).thenReturn(MULTIPLAYER_DROP_EVENT);
        when(equipmentService.removeItems(ITEM_NAME, amount, player, response)).thenReturn(itemsFromEquipment);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);

        commandExecutor.drop(ITEM_NAME, amount, player, new PlayerResponse());

        verify(eventService).getEvent(MULTIPLAYER_DROP_EVENT_KEY, PLAYER_NAME, amountStr, ITEM_NAME);
        verify(notificationService).broadcast(location, MULTIPLAYER_DROP_EVENT, player);
    }

    @Test
    public void notifiesPlayer() throws Exception {
        int amount = 2;
        String amountStr = "2";
        Player player = dummyPlayer();
        Location location = dummyLocation();
        PlayerResponse response = new PlayerResponse();
        List<Item> itemsFromEquipment = Lists.newArrayList(dummyItem());
        when(eventService.getEvent(DROP_EVENT_KEY, amountStr, ITEM_NAME)).thenReturn(DROP_EVENT);
        when(equipmentService.removeItems(ITEM_NAME, amount, player, response)).thenReturn(itemsFromEquipment);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);

        commandExecutor.drop(ITEM_NAME, amount, player, response);

        verify(eventService).getEvent(DROP_EVENT_KEY, amountStr, ITEM_NAME);
        assertThat(response.getPlayerEvents(), hasSize(1));
        assertThat(response.getPlayerEvents(), hasItem(DROP_EVENT));
    }

    @Test(expected = InvalidAmountException.class)
    public void throwsExceptionWhenDroppingZeroItems() throws Exception {
        commandExecutor.drop(ITEM_NAME, 0, dummyPlayer(), new PlayerResponse());
    }

    @Test(expected = InvalidAmountException.class)
    public void throwsExceptionWhenDroppingNegativeNumberOfItems() throws Exception {
        commandExecutor.drop(ITEM_NAME, -1, dummyPlayer(), new PlayerResponse());
    }

    private Player dummyPlayer() {
        return Player.builder()
                .name(PLAYER_NAME)
                .currentLocation(LOCATION_ID)
                .build();
    }

    private Item dummyItem() {
        return Armor.builder().build();
    }

    private Location dummyLocation() {
        return Location.builder().build();
    }
}
