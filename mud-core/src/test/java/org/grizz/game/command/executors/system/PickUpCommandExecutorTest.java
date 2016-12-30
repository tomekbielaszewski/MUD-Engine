package org.grizz.game.command.executors.system;

import com.google.common.collect.Lists;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.Script;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.LocationService;
import org.grizz.game.service.script.ScriptBinding;
import org.grizz.game.service.script.ScriptRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PickUpCommandExecutorTest {
    private static final String ITEM_NAME = "item";
    private static final String PICK_UP_EVENT = "pick up event";
    private static final String PICK_UP_EVENT_KEY = "event.player.picked.up.items";
    private static final String LOCATION_ID = "location id";
    private static final String SCRIPT_ID = "script-id";
    @Mock
    private LocationService locationService;
    @Mock
    private EquipmentService equipmentService;
    @Mock
    private LocationRepo locationRepo;
    @Mock
    private EventService eventService;

    @Mock
    private ScriptRepo scriptRepo;
    @Mock
    private ScriptRunner scriptRunner;

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

    @Test
    public void runsBeforePickUpEventWhenExist() throws Exception {
        Player player = dummyPlayer();
        Location location = dummyLocation();
        location.setBeforePickUpScript(SCRIPT_ID);
        Script script = dummyScript();
        PlayerResponse response = new PlayerResponse();
        List<ScriptBinding> bindings = Lists.newArrayList(
                ScriptBinding.builder().name("itemName").object(ITEM_NAME).build(),
                ScriptBinding.builder().name("amount").object(1).build()
        );
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(scriptRepo.get(SCRIPT_ID)).thenReturn(script);

        commandExecutor.pickUp(ITEM_NAME, 1, player, response);

        verify(scriptRepo).get(SCRIPT_ID);
        verify(scriptRunner).execute(script, player, response, Boolean.class, bindings);
    }

    @Test
    public void runsOnPickupEventWhenExist() throws Exception {
        Player player = dummyPlayer();
        Location location = dummyLocation();
        location.setOnPickUpScript(SCRIPT_ID);
        Script script = dummyScript();
        PlayerResponse response = new PlayerResponse();
        List<ScriptBinding> bindings = Lists.newArrayList(
                ScriptBinding.builder().name("itemName").object(ITEM_NAME).build(),
                ScriptBinding.builder().name("amount").object(1).build()
        );
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(scriptRepo.get(SCRIPT_ID)).thenReturn(script);

        commandExecutor.pickUp(ITEM_NAME, 1, player, response);

        verify(scriptRepo).get(SCRIPT_ID);
        verify(scriptRunner).execute(script, player, response, Boolean.class, bindings);
    }

    @Test
    public void doesNotRunAnyScriptsWhenNotExist() throws Exception {
        Player player = dummyPlayer();
        Location location = dummyLocation();
        PlayerResponse response = new PlayerResponse();
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);

        commandExecutor.pickUp(ITEM_NAME, 1, player, response);

        verify(scriptRepo, never()).get(any());
        verify(scriptRunner, never()).execute(any(), any(), any(), any(), any());
    }

    @Test
    public void doesNotPickUpItemsWhenBeforePickupEventIsNotAllowingTo() {
        Player player = dummyPlayer();
        Location location = dummyLocation();
        location.setBeforePickUpScript(SCRIPT_ID);
        Script script = dummyScript();
        PlayerResponse response = new PlayerResponse();
        when(locationRepo.get(LOCATION_ID)).thenReturn(location);
        when(scriptRepo.get(SCRIPT_ID)).thenReturn(script);
        when((scriptRunner.execute(eq(script), eq(player), eq(response), eq(Boolean.class), any()))).thenReturn(false);

        commandExecutor.pickUp(ITEM_NAME, 1, player, response);

        verify(locationService, never()).removeItems(any(), anyInt(), any());
        verify(equipmentService, never()).addItems(any(), any(), any());
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
        return Location.builder().id(LOCATION_ID).build();
    }

    private Item dummyItem() {
        return Armor.builder().name(ITEM_NAME).build();
    }

    private Script dummyScript() {
        return Script.builder().id(SCRIPT_ID).build();
    }
}