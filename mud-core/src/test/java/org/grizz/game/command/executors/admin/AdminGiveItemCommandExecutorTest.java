package org.grizz.game.command.executors.admin;

import com.google.common.collect.Lists;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.PlayerDoesNotExistException;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.repository.PlayerRepository;
import org.grizz.game.service.AdminRightsService;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.EventService;
import org.grizz.game.service.notifier.MultiplayerNotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminGiveItemCommandExecutorTest {
    private static final String PLAYER_NAME = "player";
    private static final String ADMIN_NAME = "admin";
    private static final String ITEM_NAME = "someItem";
    private static final String PLAYER_NOTIFICATION_EVENT = "player notification event";

    @Mock
    private EventService eventService;
    @Mock
    private MultiplayerNotificationService notificationService;
    @Mock
    private AdminRightsService adminRightsService;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private AdminGiveItemCommandExecutor executor = new AdminGiveItemCommandExecutor();

    @Test
    public void playerReceivesSingleItem() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);
        Player player = dummyPlayer(PLAYER_NAME);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);

        executor.give(PLAYER_NAME, ITEM_NAME, 1, admin, adminResponse);

        verify(equipmentService).addItems(eq(ITEM_NAME), eq(1), eq(player), any());
        verify(playerRepository).update(player);
    }

    @Test(expected = InvalidAmountException.class)
    public void playerCannotReceiveZeroItems() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);

        executor.give(PLAYER_NAME, ITEM_NAME, 0, admin, adminResponse);
    }

    @Test(expected = InvalidAmountException.class)
    public void playerCannotReceiveNegativeNumberOfItems() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);

        executor.give(PLAYER_NAME, ITEM_NAME, -1, admin, adminResponse);
    }

    @Test
    public void playerReceivesMultipleItems() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);
        Player player = dummyPlayer(PLAYER_NAME);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);

        executor.give(PLAYER_NAME, ITEM_NAME, 10, admin, adminResponse);

        verify(equipmentService).addItems(eq(ITEM_NAME), eq(10), eq(player), any());
        verify(playerRepository).update(player);
    }

    @Test
    public void checksAdminRights() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);
        Player player = dummyPlayer(PLAYER_NAME);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);

        executor.give(PLAYER_NAME, ITEM_NAME, 1, admin, adminResponse);

        verify(adminRightsService).checkAdminRights(admin);
    }

    @Test
    public void notifiesPlayerAboutReceivedItemsFromAdmin() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        PlayerResponse playerResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);
        Player player = dummyPlayer(PLAYER_NAME);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(player);
        when(eventService.getEvent("admin.command.items.received.from", ADMIN_NAME)).thenReturn(PLAYER_NOTIFICATION_EVENT);
        playerResponse.getPlayerEvents().add(PLAYER_NOTIFICATION_EVENT);

        executor.give(PLAYER_NAME, ITEM_NAME, 1, admin, adminResponse);

        verify(notificationService).send(player, playerResponse);
        verify(eventService).getEvent("admin.command.items.given.to", PLAYER_NAME, ITEM_NAME, "1");
        assertThat(adminResponse.getPlayerEvents(), is(not(empty())));
    }

    @Test
    public void doesNotNotifyAdminAboutReceivedItemsWhenAdminIsGivingItemsHimself() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);
        when(playerRepository.findByNameIgnoreCase(ADMIN_NAME)).thenReturn(admin);

        executor.give(ADMIN_NAME, ITEM_NAME, 1, admin, adminResponse);

        verify(equipmentService).addItems(ITEM_NAME, 1, admin, new PlayerResponse());
        assertThat(adminResponse.getPlayerEvents(), is(empty()));
    }

    @Test(expected = PlayerDoesNotExistException.class)
    public void throwsExceptionWhenUserNotExist() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);
        when(playerRepository.findByNameIgnoreCase(PLAYER_NAME)).thenReturn(null);

        executor.give(PLAYER_NAME, ITEM_NAME, 1, admin, adminResponse);
    }

    private Player dummyPlayer(String name) {
        return Player.builder()
                .name(name)
                .equipment(Equipment.builder()
                        .backpack(Lists.newArrayList())
                        .build())
                .build();
    }
}
