package org.grizz.game.command.executors;

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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RunWith(MockitoJUnitRunner.class)
public class DropCommandExecutorTest {
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
        throw new NotImplementedException();
    }

    @Test
    public void notifiesOtherPlayersOnLocation() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void throwsExceptionWhenDroppingZeroItems() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void throwsExceptionWhenDroppingNegativeNumberOfItems() throws Exception {
        throw new NotImplementedException();
    }
}