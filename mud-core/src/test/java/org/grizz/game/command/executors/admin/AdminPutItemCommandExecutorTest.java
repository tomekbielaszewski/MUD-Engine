package org.grizz.game.command.executors.admin;

import com.google.common.collect.Lists;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Static;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.AdminRightsService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.LocationService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminPutItemCommandExecutorTest {
    private static final String ITEM_NAME = "item name";
    private static final String LOCATION_ID = "location id";
    private static final String ADMIN_NAME = "admin";
    private static final String ITEMS_BROADCAST_EVENT = "items put on location broadcast";
    private static final String ITEMS_ADMIN_NOTIFICATION_EVENT = "admin put item notification";
    private static final String STATIC_ADMIN_NOTIFICATION_EVENT = "admin put static item notification";
    private static final String STATIC_BROADCAST_EVENT = "static put on location broadcast";

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Mock
    private LocationRepo locationRepo;
    @Mock
    private LocationService locationService;
    @Mock
    private EventService eventService;
    @Mock
    private MultiplayerNotificationService notificationService;
    @Mock
    private ItemRepo itemRepo;
    @Mock
    private AdminRightsService adminRightsService;

    @InjectMocks
    private AdminPutItemCommandExecutor commandExecutor = new AdminPutItemCommandExecutor();

    @Test
    public void putsMobileItemsOnLocation() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        Item item = dummyItem(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);

        commandExecutor.put(ITEM_NAME, 1, admin, response);

        verify(locationService).addItems(ITEM_NAME, 1, location);
    }

    @Test
    public void putsStaticItemOnLocation() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        Item item = dummyStatic(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);

        commandExecutor.put(ITEM_NAME, 1, admin, response);

        verify(locationService).addStaticItem(item, location);
    }

    @Test
    public void checksAdminRights() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        Item item = dummyItem(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);

        commandExecutor.put(ITEM_NAME, 1, admin, response);

        verify(adminRightsService).checkAdminRights(admin);
    }

    @Test
    public void notifiesAdminAboutAddedItems() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        Item item = dummyItem(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(eventService.getEvent("admin.command.item.put.notification", ITEM_NAME, "2")).thenReturn(ITEMS_ADMIN_NOTIFICATION_EVENT);

        commandExecutor.put(ITEM_NAME, 2, admin, response);

        assertThat(response.getPlayerEvents(), hasItem(ITEMS_ADMIN_NOTIFICATION_EVENT));
    }

    @Test
    public void notifiesPlayersOnLocationAboutAddedItems() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        Item item = dummyItem(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(eventService.getEvent("admin.command.item.put.notification.broadcast", ITEM_NAME, "2", ADMIN_NAME)).thenReturn(ITEMS_BROADCAST_EVENT);

        commandExecutor.put(ITEM_NAME, 2, admin, response);

        verify(notificationService).broadcast(location, ITEMS_BROADCAST_EVENT, admin);
    }

    @Test
    public void notifiesAdminAboutAddedStaticItem() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        Item item = dummyStatic(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(eventService.getEvent("admin.command.static.item.add.notification", ITEM_NAME)).thenReturn(STATIC_ADMIN_NOTIFICATION_EVENT);

        commandExecutor.put(ITEM_NAME, 2, admin, response);

        assertThat(response.getPlayerEvents(), hasItem(STATIC_ADMIN_NOTIFICATION_EVENT));
    }

    @Test
    public void notifiesPlayersOnLocationAboutAddedStaticItem() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        Item item = dummyStatic(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(eventService.getEvent("admin.command.static.item.add.notification.broadcast", ITEM_NAME, ADMIN_NAME)).thenReturn(STATIC_BROADCAST_EVENT);

        commandExecutor.put(ITEM_NAME, 1, admin, response);

        verify(notificationService).broadcast(location, STATIC_BROADCAST_EVENT, admin);
    }

    @Test
    public void throwsExceptionWhenPuttingZeroItems() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        Item item = dummyItem(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        expectedException.expect(InvalidAmountException.class);

        commandExecutor.put(ITEM_NAME, 0, admin, response);

        verify(locationService, never()).addItems(any(), anyInt(), any());
        verify(locationService, never()).addStaticItem(any(), any());
    }

    @Test
    public void throwsExceptionWhenPuttingNegativeNumberOfItems() throws Exception {
        Player admin = dummyPlayer();
        PlayerResponse response = new PlayerResponse();
        Location location = dummyLocation();
        Item item = dummyItem(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        expectedException.expect(InvalidAmountException.class);

        commandExecutor.put(ITEM_NAME, -1, admin, response);

        verify(locationService, never()).addItems(any(), anyInt(), any());
        verify(locationService, never()).addStaticItem(any(), any());
    }

    private Item dummyItem(String itemName) {
        return Armor.builder().name(itemName).build();
    }

    private Item dummyStatic(String itemName) {
        return Static.builder().name(itemName).build();
    }

    private Location dummyLocation() {
        return Location.builder()
                .items(LocationItems.builder()
                        .mobileItems(Lists.newArrayList())
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
    }

    private Player dummyPlayer() {
        return Player.builder().name(ADMIN_NAME).currentLocation(LOCATION_ID).build();
    }
}