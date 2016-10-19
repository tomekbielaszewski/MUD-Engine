package org.grizz.game.service;

import com.google.common.collect.Lists;
import org.grizz.game.exception.CantOwnStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Player;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Static;
import org.grizz.game.model.repository.ItemRepo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentServiceTest {
    private static final String ITEM_NAME = "item name";
    private static final String NOT_RELEVANT_ITEM_NAME = "another item name";
    private static final String STATIC_ITEM_NAME = "static item";
    private static final String ITEM_REMOVE_EVENT_KEY = "event.player.lost.items";
    private static final String ITEM_REMOVE_EVENT = "straciles przedmiot...";

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ItemRepo itemRepo;
    @Mock
    private EventService eventService;

    @InjectMocks
    private EquipmentService equipmentService = new EquipmentService();

    @Test
    public void removesOneItemFromBackpack() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        PlayerResponse response = new PlayerResponse();
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));
        when(eventService.getEvent(ITEM_REMOVE_EVENT_KEY, "1", ITEM_NAME)).thenReturn(ITEM_REMOVE_EVENT);

        List<Item> result = equipmentService.takeOutItems(ITEM_NAME, 1, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItems(
                dummyItem(ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(dummyItem(ITEM_NAME)));
        verify(eventService).getEvent(ITEM_REMOVE_EVENT_KEY, "1", ITEM_NAME);
        assertThat(response.getPlayerEvents(), hasItem(ITEM_REMOVE_EVENT));
    }

    @Test
    public void removesFromBackpackSomeOfPresentItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        PlayerResponse response = new PlayerResponse();
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));
        when(eventService.getEvent(ITEM_REMOVE_EVENT_KEY, "2", ITEM_NAME)).thenReturn(ITEM_REMOVE_EVENT);

        List<Item> result = equipmentService.takeOutItems(ITEM_NAME, 2, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItems(
                dummyItem(ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        assertThat(result, hasSize(2));
        assertThat(result, hasItem(dummyItem(ITEM_NAME)));
        assertThat(result, not(hasItem(dummyItem(NOT_RELEVANT_ITEM_NAME))));
        verify(eventService).getEvent(ITEM_REMOVE_EVENT_KEY, "2", ITEM_NAME);
        assertThat(response.getPlayerEvents(), hasItem(ITEM_REMOVE_EVENT));
    }

    @Test
    public void removesFromBackpackAllItemsAndLeavesEmptyBackpack() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME)
        ));
        PlayerResponse response = new PlayerResponse();
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));
        when(eventService.getEvent(ITEM_REMOVE_EVENT_KEY, "3", ITEM_NAME)).thenReturn(ITEM_REMOVE_EVENT);

        List<Item> result = equipmentService.takeOutItems(ITEM_NAME, 3, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(0));
        assertThat(result, hasSize(3));
        assertThat(result, hasItem(dummyItem(ITEM_NAME)));
        assertThat(result, not(hasItem(dummyItem(NOT_RELEVANT_ITEM_NAME))));
        verify(eventService).getEvent(ITEM_REMOVE_EVENT_KEY, "3", ITEM_NAME);
        assertThat(response.getPlayerEvents(), hasItem(ITEM_REMOVE_EVENT));
    }

    @Test
    public void throwsExceptionWhenTakingOutTooManyItemsWhichPlayerHave() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME)
        ));
        PlayerResponse response = new PlayerResponse();
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));
        expectedException.expect(NotEnoughItemsException.class);

        equipmentService.takeOutItems(ITEM_NAME, 3, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(ITEM_NAME)));
    }

    @Test
    public void throwsExceptionWhenTakingOutItemsWhichPlayerDoNotHave() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(NOT_RELEVANT_ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        PlayerResponse response = new PlayerResponse();
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(dummyItem(ITEM_NAME));
        expectedException.expect(NoSuchItemException.class);

        equipmentService.takeOutItems(ITEM_NAME, 1, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(NOT_RELEVANT_ITEM_NAME)));
    }

    @Test
    public void throwsExceptionWhenTakingOutNegativeNumberOfItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        expectedException.expect(InvalidAmountException.class);

        equipmentService.takeOutItems(ITEM_NAME, -1, player, response);
    }

    @Test
    public void throwsExceptionWhenTakingOutZeroItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        expectedException.expect(InvalidAmountException.class);

        equipmentService.takeOutItems(ITEM_NAME, 0, player, response);
    }

    @Test
    public void throwsExceptionWhenTakingOutStaticItem() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        when(itemRepo.getByName(STATIC_ITEM_NAME)).thenReturn(dummyStaticItem());
        expectedException.expect(CantOwnStaticItemException.class);

        equipmentService.takeOutItems(STATIC_ITEM_NAME, 1, player, response);
    }

    private Item dummyStaticItem() {
        return Static.builder().build();
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