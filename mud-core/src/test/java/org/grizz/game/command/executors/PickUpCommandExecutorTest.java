package org.grizz.game.command.executors;

import com.google.common.collect.Lists;
import org.grizz.game.model.Location;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PickUpCommandExecutorTest {
    private static final String ITEM_NAME = "item";
    @Mock
    private LocationService locationService;
    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private PickUpCommandExecutor commandExecutor = new PickUpCommandExecutor();

    @Test
    public void picksUpItemFromLocationAndPassesToEquipmentService() throws Exception {
        Player player = dummyPlayer();
        Location location = dummyLocation();
        PlayerResponse response = new PlayerResponse();
        List<Item> itemsToPickUp = Lists.newArrayList(dummyItem());
        when(locationService.pickItems(ITEM_NAME, 1, location)).thenReturn(itemsToPickUp);

        commandExecutor.pickUp(ITEM_NAME, 1, player, response);

        verify(locationService).pickItems(ITEM_NAME, 1, location);
        verify(equipmentService).insertItems(itemsToPickUp, player, response);
    }

    @Test
    public void notifiesPlayerAboutPickedUpItems() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void throwsExceptionWhenPickingUpZeroItems() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void throwsExceptionWhenPickingUpNegativeNumberOfItems() throws Exception {
        throw new NotImplementedException();
    }

    private Player dummyPlayer() {
        return Player.builder().build();
    }

    private Location dummyLocation() {
        return Location.builder().build();
    }

    private Item dummyItem() {
        return Armor.builder().build();
    }
}