package org.grizz.game.integration.tests.commands;

import org.grizz.game.integration.GameIntegrationTest;
import org.grizz.game.model.items.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(SpringJUnit4ClassRunner.class)
public class AdminGiveItemCommand extends GameIntegrationTest {

    @Test
    public void cantExecuteAsNotAdmin() throws Exception {
        List<Item> playerBackpackBefore = fromDB().player(PLAYER1).getEquipment().getBackpack();

        player1("admin daj zlota moneta");

        List<Item> playerBackpackAfter = fromDB().player(PLAYER1).getEquipment().getBackpack();

        assertThat(playerBackpackBefore, hasSize(playerBackpackAfter.size()));
        assertThat(playerBackpackBefore, hasItems(playerBackpackAfter.toArray(new Item[]{})));
        assertThat(response, hasEvent("Nie mozesz tego zrobic. Nie jestes administratorem"));
        verifyZeroInteractions(notifier);
    }

    @Test
    public void cantGiveStaticItemToAPlayer() throws Exception {
        List<Item> playerBackpackBefore = fromDB().player(PLAYER1).getEquipment().getBackpack();

        admin("admin daj kowadlo graczowi " + PLAYER1);

        List<Item> playerBackpackAfter = fromDB().player(PLAYER1).getEquipment().getBackpack();

        assertThat(playerBackpackBefore, hasSize(playerBackpackAfter.size()));
        assertThat(playerBackpackBefore, hasItems(playerBackpackAfter.toArray(new Item[]{})));
        assertThat(response, hasEvent("Nie mozesz dac statycznego przedmiotu!"));
        verifyZeroInteractions(notifier);
    }

    @Test
    public void cantGiveStaticItemToHimself() throws Exception {
        List<Item> playerBackpackBefore = fromDB().player(PLAYER1).getEquipment().getBackpack();

        admin("admin daj kowadlo");

        List<Item> playerBackpackAfter = fromDB().player(PLAYER1).getEquipment().getBackpack();

        assertThat(playerBackpackBefore, hasSize(playerBackpackAfter.size()));
        assertThat(playerBackpackBefore, hasItems(playerBackpackAfter.toArray(new Item[]{})));
        assertThat(response, hasEvent("Nie mozesz dac statycznego przedmiotu!"));
        verifyZeroInteractions(notifier);
    }
}
