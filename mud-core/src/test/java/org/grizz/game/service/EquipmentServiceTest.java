package org.grizz.game.service;

import com.google.common.collect.Lists;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentServiceTest {
    private static final String ITEM_NAME = "item name";
    private static final String NOT_RELEVANT_ITEM_NAME = "another item name";

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private EquipmentService equipmentService = new EquipmentService();

    @Test
    public void removesOneItemFromBackpack() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        when(itemRepo.get(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));

        List<Item> result = equipmentService.takeOutItems(ITEM_NAME, 1, player);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItems(
                dummyItem(ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(dummyItem(ITEM_NAME)));
    }

    @Test
    public void removesFromBackpackSomeOfPresentItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        when(itemRepo.get(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));

        List<Item> result = equipmentService.takeOutItems(ITEM_NAME, 2, player);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItems(
                dummyItem(ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        assertThat(result, hasSize(2));
        assertThat(result, hasItem(dummyItem(ITEM_NAME)));
        assertThat(result, not(hasItem(dummyItem(NOT_RELEVANT_ITEM_NAME))));
    }

    @Test
    public void removesFromBackpackAllItemsAndLeavesEmptyBackpack() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME)
        ));
        when(itemRepo.get(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));

        List<Item> result = equipmentService.takeOutItems(ITEM_NAME, 3, player);

        assertThat(player.getEquipment().getBackpack(), hasSize(0));
        assertThat(result, hasSize(3));
        assertThat(result, hasItem(dummyItem(ITEM_NAME)));
        assertThat(result, not(hasItem(dummyItem(NOT_RELEVANT_ITEM_NAME))));
    }

    @Test(expected = NotEnoughItemsException.class)
    public void throwsExceptionWhenTakingOutTooManyItemsWhichPlayerHave() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME)
        ));
        when(itemRepo.get(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));

        equipmentService.takeOutItems(ITEM_NAME, 3, player);
    }

    @Test(expected = NotEnoughItemsException.class)
    public void throwsExceptionWhenTakingOutItemsWhichPlayerDoNotHave() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(NOT_RELEVANT_ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        when(itemRepo.get(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));

        equipmentService.takeOutItems(ITEM_NAME, 3, player);
    }

    @Test(expected = InvalidAmountException.class)
    public void throwsExceptionWhenTakingOutNegativeNumberOfItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());

        equipmentService.takeOutItems(ITEM_NAME, -1, player);
    }

    @Test(expected = InvalidAmountException.class)
    public void throwsExceptionWhenTakingOutZeroItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());

        equipmentService.takeOutItems(ITEM_NAME, 0, player);
    }

    private Player dummyPlayer(List<Item> backpack) {
        return Player.builder()
                .equipment(Equipment.builder()
                        .backpack(backpack)
                        .build())
                .build();
    }

    private Item dummyItem(String itemName) {
        return Armor.builder().name(itemName).build();
    }
}