package org.grizz.game.command.executors;

import com.google.common.collect.Lists;
import org.grizz.game.command.executors.admin.AdminGiveItemCommandExecutor;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

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

    @InjectMocks
    private AdminGiveItemCommandExecutor executor = new AdminGiveItemCommandExecutor();

    @Test
    public void playerReceivesDesiredSingleItem() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Item item = dummyItem(ITEM_NAME);
        Player admin = dummyPlayer(ADMIN_NAME);
        Player player = dummyPlayer(PLAYER_NAME);

        executor.give(PLAYER_NAME, ITEM_NAME, 1, admin, adminResponse);

        assertThat(player.getEquipment().getBackpack(), hasSize(1));
        assertThat(player.getEquipment().getBackpack(), hasItem(item));
        assertThat(adminResponse.getPlayerEvents(), is(not(empty())));
    }

    @Test
    public void playerReceivesDesiredMultipleItems() throws Exception {
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
    public void desiredItemDoesNotExistAndPlayersBackpackIsNotChanged() throws Exception {
        PlayerResponse adminResponse = new PlayerResponse();
        Player admin = dummyPlayer(ADMIN_NAME);
        Player player = dummyPlayer(PLAYER_NAME);

        executor.give(PLAYER_NAME, ITEM_NAME, 1, admin, adminResponse);

        assertThat(player.getEquipment().getBackpack(), hasSize(0));
        assertThat(adminResponse.getPlayerEvents(), is(not(empty())));
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
