package org.grizz.game.command.executors.admin;

import old.org.grizz.game.service.complex.MultiplayerNotificationService;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.AdminRightsService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RunWith(MockitoJUnitRunner.class)
public class AdminPutItemCommandExecutorTest {
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
        throw new NotImplementedException();
    }

    @Test
    public void putsStaticItemOnLocation() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void savesLocationState() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void checksAdminRights() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void notifiesAdmin() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void notifiesPlayersOnLocation() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void throwsExceptionWhenPuttingMultipleStaticItems() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void throwsExceptionWhenPuttingZeroItems() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void throwsExceptionWhenPuttingNegativeNumberOfItems() throws Exception {
        throw new NotImplementedException();
    }
}