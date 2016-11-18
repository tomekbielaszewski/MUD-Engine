package org.grizz.game.command.executors;

import com.google.common.collect.Lists;
import org.grizz.game.command.executors.admin.AdminGiveItemCommandExecutor;
import org.grizz.game.exception.CantGiveStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.service.EventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class AdminGiveItemCommandExecutorTest {
    private static final String PLAYER_NAME = "player";
    private static final String ADMIN_NAME = "admin";
    private static final String ITEM_NAME = "someItem";
    private static final String STATIC_ITEM_NAME = "staticItem";

    @Mock
    private EventService eventService;

    @InjectMocks
    private AdminGiveItemCommandExecutor executor = new AdminGiveItemCommandExecutor();

    @Test
    public void playerReceivesSingleItem() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Item item = dummyItem(ITEM_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        Player player = dummyPlayer(PLAYER_NAME);

        executor.give(PLAYER_NAME, ITEM_NAME, 1, admin, adminResponse);

        assertThat(player.getEquipment().getBackpack(), hasSize(1));
        assertThat(player.getEquipment().getBackpack(), hasItem(item));
        assertThat(adminResponse.getPlayerEvents(), is(not(empty())));
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

    @Test(expected = CantGiveStaticItemException.class)
    public void playerCannotReceiveStaticItems() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);

        executor.give(PLAYER_NAME, STATIC_ITEM_NAME, 1, admin, adminResponse);
    }

    @Test
    public void playerReceivesMultipleItems() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Item item = dummyItem(ITEM_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        Player player = dummyPlayer(PLAYER_NAME);

        executor.give(PLAYER_NAME, ITEM_NAME, 10, admin, adminResponse);

        assertThat(player.getEquipment().getBackpack(), hasSize(10));
        assertThat(player.getEquipment().getBackpack(), everyItem(is(item)));
        assertThat(adminResponse.getPlayerEvents(), is(not(empty())));
    }

    @Test
    public void whenItemDoesNotExistPlayersBackpackIsNotChanged() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);
        Player player = dummyPlayer(PLAYER_NAME);

        executor.give(PLAYER_NAME, ITEM_NAME, 1, admin, adminResponse);

        assertThat(player.getEquipment().getBackpack(), hasSize(0));
        assertThat(adminResponse.getPlayerEvents(), is(not(empty())));
    }

    @Test
    public void checksAdminRights() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void notifiesPlayerAboutReceivedItemsFromAdmin() throws Exception {
        throw new NotImplementedException();
    }

    @Test
    public void doesNotNotifyAdminAboutReceivedItemsWhenAdminIsGivingItemsHimself() throws Exception {
        throw new NotImplementedException();
    }

    private Player dummyPlayer(String name) {
        return Player.builder()
                .name(name)
                .equipment(Equipment.builder()
                        .backpack(Lists.newArrayList())
                        .build())
                .build();
    }

    private Item dummyItem(String itemName) {
        return Armor.builder()
                .name(itemName)
                .build();
    }
}
