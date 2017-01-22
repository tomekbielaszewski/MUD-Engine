package org.grizz.game.service;

import com.google.common.collect.Lists;
import org.grizz.game.exception.CantOwnStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.*;
import org.grizz.game.model.converters.ItemListToItemStackConverter;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Static;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptBinding;
import org.grizz.game.service.script.ScriptRunner;
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
import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EquipmentServiceTest {
    private static final String ITEM_NAME = "item name";
    private static final String ITEM_NAME_2 = "item name 2";
    private static final String NOT_RELEVANT_ITEM_NAME = "another item name";
    private static final String STATIC_ITEM_NAME = "static item";
    private static final String ITEM_REMOVE_EVENT_KEY = "event.player.lost.items";
    private static final String ITEM_REMOVE_EVENT = "straciles przedmiot...";
    private static final String ITEMS_RECEIVED_EVENT_KEY_HEADER = "event.player.received.items.header";
    private static final String ITEMS_RECEIVED_EVENT_KEY_SINGLE_ENTRY = "event.player.received.items.single.entry";
    private static final String ITEMS_RECEIVED_EVENT_MESSAGE_HEADER = "Otrzymales przedmioty:";
    private static final String ITEMS_RECEIVED_EVENT_MESSAGE_SINGLE_ENTRY_1 = "przedmiot pierwszy";
    private static final String ITEMS_RECEIVED_EVENT_MESSAGE_SINGLE_ENTRY_2 = "przedmiot drugi";
    private static final String ON_DROP_SCRIPT_ID = "onDropScriptId";
    private static final String BEFORE_DROP_SCRIPT_ID = "beforeDropScriptOd";
    private static final String ON_RECEIVE_SCRIPT_ID = "onReceiveScript";
    private static final String BEFORE_RECEIVE_SCRIPT_ID = "beforeReceiveScriptId";

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ItemRepo itemRepo;
    @Mock
    private EventService eventService;
    @Mock
    private ItemListToItemStackConverter itemStackConverter;
    @Mock
    private ScriptRunner scriptRunner;
    @Mock
    private ScriptRepo scriptRepo;

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
        setupItemRepo();
        when(eventService.getEvent(ITEM_REMOVE_EVENT_KEY, "1", ITEM_NAME)).thenReturn(ITEM_REMOVE_EVENT);

        List<Item> result = equipmentService.removeItems(ITEM_NAME, 1, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItems(
                dummyItem(ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        assertThat(result, hasSize(1));
        assertThat(result, hasItem(dummyItem(ITEM_NAME)));
        verify(eventService).getEvent(ITEM_REMOVE_EVENT_KEY, "1", ITEM_NAME);
        assertThat(response.getPlayerEvents(), hasItem(ITEM_REMOVE_EVENT));
        verify(scriptRunner, never()).execute(any(), any(), any(), any(), any());
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
        setupItemRepo();
        when(eventService.getEvent(ITEM_REMOVE_EVENT_KEY, "2", ITEM_NAME)).thenReturn(ITEM_REMOVE_EVENT);

        List<Item> result = equipmentService.removeItems(ITEM_NAME, 2, player, response);

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
        verify(scriptRunner, never()).execute(any(), any(), any(), any(), any());
    }

    @Test
    public void removesFromBackpackAllItemsAndLeavesEmptyBackpack() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME)
        ));
        PlayerResponse response = new PlayerResponse();
        setupItemRepo();
        when(eventService.getEvent(ITEM_REMOVE_EVENT_KEY, "3", ITEM_NAME)).thenReturn(ITEM_REMOVE_EVENT);

        List<Item> result = equipmentService.removeItems(ITEM_NAME, 3, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(0));
        assertThat(result, hasSize(3));
        assertThat(result, hasItem(dummyItem(ITEM_NAME)));
        assertThat(result, not(hasItem(dummyItem(NOT_RELEVANT_ITEM_NAME))));
        verify(eventService).getEvent(ITEM_REMOVE_EVENT_KEY, "3", ITEM_NAME);
        assertThat(response.getPlayerEvents(), hasItem(ITEM_REMOVE_EVENT));
        verify(scriptRunner, never()).execute(any(), any(), any(), any(), any());
    }

    @Test
    public void executesOnDropScriptWhenRemovingItems() throws Exception {
        int amount = 1;
        Item item = dummyItemBuilder(ITEM_NAME).onDropScript(ON_DROP_SCRIPT_ID).build();
        Player player = dummyPlayer(Lists.newArrayList(item));
        PlayerResponse response = new PlayerResponse();
        setupItemRepo(item);
        setupScriptRepo();

        equipmentService.removeItems(ITEM_NAME, amount, player, response);

        verify(scriptRunner).execute(dummyScript(ON_DROP_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, amount));
        verifyScriptRunnerNeverCalledWith(BEFORE_DROP_SCRIPT_ID, ON_RECEIVE_SCRIPT_ID, BEFORE_RECEIVE_SCRIPT_ID);
    }

    @Test
    public void executesBeforeDropScriptWhenRemovingItems() throws Exception {
        int amount = 1;
        Item item = dummyItemBuilder(ITEM_NAME).beforeDropScript(BEFORE_DROP_SCRIPT_ID).build();
        Player player = dummyPlayer(Lists.newArrayList(item));
        PlayerResponse response = new PlayerResponse();
        setupItemRepo(item);
        setupScriptRepo();

        equipmentService.removeItems(ITEM_NAME, amount, player, response);

        verify(scriptRunner).execute(dummyScript(BEFORE_DROP_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, amount));
        verifyScriptRunnerNeverCalledWith(ON_DROP_SCRIPT_ID, ON_RECEIVE_SCRIPT_ID, BEFORE_RECEIVE_SCRIPT_ID);
    }

    @Test
    public void doesNotRemoveItemsWhenBeforeDropScriptIsNotAllowingTo() throws Exception {
        int amount = 1;
        Item item = dummyItemBuilder(ITEM_NAME).beforeDropScript(BEFORE_DROP_SCRIPT_ID).build();
        Player player = dummyPlayer(Lists.newArrayList(
                item, dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        PlayerResponse response = new PlayerResponse();
        setupItemRepo(item);
        setupScriptRepo();
        when(scriptRunner.execute(dummyScript(BEFORE_DROP_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, amount))).thenReturn(false);

        List<Item> result = equipmentService.removeItems(ITEM_NAME, amount, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItems(
                item,
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        assertThat(result, hasSize(0));
        verify(scriptRunner).execute(dummyScript(BEFORE_DROP_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, amount));
        verifyScriptRunnerNeverCalledWith(ON_DROP_SCRIPT_ID, ON_RECEIVE_SCRIPT_ID, BEFORE_RECEIVE_SCRIPT_ID);
    }

    @Test
    public void executesOnReceiveScriptWhenAddingItemsByName() throws Exception {
        int amount = 1;
        Item item = dummyItemBuilder(ITEM_NAME).onReceiveScript(ON_RECEIVE_SCRIPT_ID).build();
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        setupItemRepo(item);
        setupScriptRepo();

        boolean canReceive = equipmentService.addItems(ITEM_NAME, amount, player, response);

        verify(scriptRunner).execute(dummyScript(ON_RECEIVE_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, amount));
        verifyScriptRunnerNeverCalledWith(ON_DROP_SCRIPT_ID, BEFORE_DROP_SCRIPT_ID, BEFORE_RECEIVE_SCRIPT_ID);
        assertTrue(canReceive);
    }

    @Test
    public void executesBeforeReceiveScriptWhenAddingItemsByName() throws Exception {
        int amount = 1;
        Item item = dummyItemBuilder(ITEM_NAME).beforeReceiveScript(BEFORE_RECEIVE_SCRIPT_ID).build();
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        setupItemRepo(item);
        setupScriptRepo();

        boolean canReceive = equipmentService.addItems(ITEM_NAME, amount, player, response);

        verify(scriptRunner).execute(dummyScript(BEFORE_RECEIVE_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, amount));
        verifyScriptRunnerNeverCalledWith(ON_DROP_SCRIPT_ID, BEFORE_DROP_SCRIPT_ID, ON_RECEIVE_SCRIPT_ID);
        assertTrue(canReceive);
    }

    @Test
    public void executesOnReceiveScriptWhenAddingListOfDifferentItems() throws Exception {
        Item irrelevantItem = dummyItem(ITEM_NAME_2);
        Item item = dummyItemBuilder(ITEM_NAME).onReceiveScript(ON_RECEIVE_SCRIPT_ID).build();
        List<Item> items = Lists.newArrayList(irrelevantItem, item);
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        setupScriptRepo();

        boolean canReceive = equipmentService.addItems(items, player, response);

        verify(scriptRunner).execute(dummyScript(ON_RECEIVE_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, 1));
        verifyScriptRunnerNeverCalledWith(ON_DROP_SCRIPT_ID, BEFORE_DROP_SCRIPT_ID, BEFORE_RECEIVE_SCRIPT_ID);
        assertTrue(canReceive);
    }

    @Test
    public void executesBeforeReceiveScriptWhenAddingListOfDifferentItems() throws Exception {
        Item irrelevantItem = dummyItem(ITEM_NAME_2);
        Item item = dummyItemBuilder(ITEM_NAME).beforeReceiveScript(BEFORE_RECEIVE_SCRIPT_ID).build();
        List<Item> items = Lists.newArrayList(irrelevantItem, item);
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        setupScriptRepo();

        boolean canReceive = equipmentService.addItems(items, player, response);

        verify(scriptRunner).execute(dummyScript(BEFORE_RECEIVE_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, 1));
        verifyScriptRunnerNeverCalledWith(ON_DROP_SCRIPT_ID, BEFORE_DROP_SCRIPT_ID, ON_RECEIVE_SCRIPT_ID);
        assertTrue(canReceive);
    }

    @Test
    public void doesNotAddItemsWhenBeforeReceiveScriptIsNotAllowingTo() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        Item item = dummyItemBuilder(ITEM_NAME).beforeReceiveScript(BEFORE_RECEIVE_SCRIPT_ID).build();
        List<Item> items = Lists.newArrayList(item);
        setupScriptRepo();
        when(itemStackConverter.convert(items)).thenReturn(Lists.newArrayList());
        when(scriptRunner.execute(dummyScript(BEFORE_RECEIVE_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, 1))).thenReturn(false);

        boolean canReceive = equipmentService.addItems(items, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(0));
        verify(scriptRunner).execute(dummyScript(BEFORE_RECEIVE_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, 1));
        assertFalse(canReceive);
    }

    @Test
    public void doesNotAddAnyItemsWhenBeforeReceiveScriptOnOneOfThemIsNotAllowingTo() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        Item irrelevantItem = dummyItem(ITEM_NAME_2);
        Item item = dummyItemBuilder(ITEM_NAME).beforeReceiveScript(BEFORE_RECEIVE_SCRIPT_ID).build();
        List<Item> items = Lists.newArrayList(irrelevantItem, irrelevantItem, item, irrelevantItem, irrelevantItem);
        setupScriptRepo();
        when(itemStackConverter.convert(items)).thenReturn(Lists.newArrayList());
        when(scriptRunner.execute(dummyScript(BEFORE_RECEIVE_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, 1))).thenReturn(false);

        boolean canReceive = equipmentService.addItems(items, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(0));
        verify(scriptRunner).execute(dummyScript(BEFORE_RECEIVE_SCRIPT_ID), player, response, Boolean.class, dummyBindings(item, 1));
        assertFalse(canReceive);
    }

    @Test
    public void throwsExceptionWhenRemovingTooManyItemsWhichPlayerHave() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(ITEM_NAME),
                dummyItem(ITEM_NAME)
        ));
        PlayerResponse response = new PlayerResponse();
        setupItemRepo();
        expectedException.expect(NotEnoughItemsException.class);

        equipmentService.removeItems(ITEM_NAME, 3, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(ITEM_NAME)));
    }

    @Test
    public void throwsExceptionWhenRemovingItemsWhichPlayerDoNotHave() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList(
                dummyItem(NOT_RELEVANT_ITEM_NAME),
                dummyItem(NOT_RELEVANT_ITEM_NAME)
        ));
        PlayerResponse response = new PlayerResponse();
        setupItemRepo();
        expectedException.expect(NoSuchItemException.class);

        equipmentService.removeItems(ITEM_NAME, 1, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(NOT_RELEVANT_ITEM_NAME)));
    }

    @Test
    public void throwsExceptionWhenRemovingNegativeNumberOfItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        expectedException.expect(InvalidAmountException.class);

        equipmentService.removeItems(ITEM_NAME, -1, player, response);
    }

    @Test
    public void throwsExceptionWhenRemovingZeroItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        expectedException.expect(InvalidAmountException.class);

        equipmentService.removeItems(ITEM_NAME, 0, player, response);
    }

    @Test
    public void throwsExceptionWhenRemovingStaticItem() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        setupItemRepo();
        expectedException.expect(CantOwnStaticItemException.class);

        equipmentService.removeItems(STATIC_ITEM_NAME, 1, player, response);
    }

    @Test
    public void addsSingleItemToEmptyBackpack() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        List<Item> items = dummyItems(ITEM_NAME);
        when(itemStackConverter.convert(items)).thenReturn(Lists.newArrayList());

        boolean canReceive = equipmentService.addItems(items, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(1));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(ITEM_NAME)));
        assertTrue(canReceive);
    }

    @Test
    public void addsSingleItemToNonEmptyBackpack() throws Exception {
        Player player = dummyPlayer(dummyItems(NOT_RELEVANT_ITEM_NAME));
        PlayerResponse response = new PlayerResponse();
        List<Item> items = dummyItems(ITEM_NAME);
        when(itemStackConverter.convert(items)).thenReturn(Lists.newArrayList());

        boolean canReceive = equipmentService.addItems(items, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(2));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(ITEM_NAME)));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(NOT_RELEVANT_ITEM_NAME)));
        assertTrue(canReceive);
    }

    @Test
    public void addsManyItemsToEmptyBackpack() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        List<Item> items = dummyItems(ITEM_NAME, ITEM_NAME, ITEM_NAME);
        when(itemStackConverter.convert(items)).thenReturn(Lists.newArrayList());

        boolean canReceive = equipmentService.addItems(items, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(3));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(ITEM_NAME)));
        assertTrue(canReceive);
    }

    @Test
    public void addsManyItemsToNonEmptyBackpack() throws Exception {
        Player player = dummyPlayer(dummyItems(NOT_RELEVANT_ITEM_NAME));
        PlayerResponse response = new PlayerResponse();
        List<Item> items = dummyItems(ITEM_NAME, ITEM_NAME, ITEM_NAME);
        when(itemStackConverter.convert(items)).thenReturn(Lists.newArrayList());

        boolean canReceive = equipmentService.addItems(items, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(4));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(ITEM_NAME)));
        assertThat(player.getEquipment().getBackpack(), hasItem(dummyItem(NOT_RELEVANT_ITEM_NAME)));
        assertTrue(canReceive);
    }

    @Test
    public void notifiesPlayerAboutEveryItemStackReceived() throws Exception {
        Player player = dummyPlayer(dummyItems());
        PlayerResponse response = new PlayerResponse();
        List<Item> items = dummyItems(ITEM_NAME, ITEM_NAME_2);
        when(itemStackConverter.convert(items)).thenReturn(Lists.newArrayList(
                ItemStack.builder().amount(1).name(ITEM_NAME).build(),
                ItemStack.builder().amount(2).name(ITEM_NAME_2).build()
        ));
        when(eventService.getEvent(ITEMS_RECEIVED_EVENT_KEY_HEADER)).thenReturn(ITEMS_RECEIVED_EVENT_MESSAGE_HEADER);
        when(eventService.getEvent(ITEMS_RECEIVED_EVENT_KEY_SINGLE_ENTRY, "2", ITEM_NAME_2))
                .thenReturn(ITEMS_RECEIVED_EVENT_MESSAGE_SINGLE_ENTRY_1);
        when(eventService.getEvent(ITEMS_RECEIVED_EVENT_KEY_SINGLE_ENTRY, "1", ITEM_NAME))
                .thenReturn(ITEMS_RECEIVED_EVENT_MESSAGE_SINGLE_ENTRY_2);

        boolean canReceive = equipmentService.addItems(items, player, response);

        assertThat(response.getPlayerEvents(), hasSize(3));
        assertThat(response.getPlayerEvents(), hasItems(ITEMS_RECEIVED_EVENT_MESSAGE_HEADER));
        assertThat(response.getPlayerEvents(), hasItems(ITEMS_RECEIVED_EVENT_MESSAGE_SINGLE_ENTRY_1));
        assertThat(response.getPlayerEvents(), hasItems(ITEMS_RECEIVED_EVENT_MESSAGE_SINGLE_ENTRY_2));
        assertTrue(canReceive);
    }

    @Test
    public void throwsExceptionWhenAddingZeroItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        List<Item> items = dummyItems();
        expectedException.expect(InvalidAmountException.class);

        equipmentService.addItems(items, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(0));
    }

    @Test
    public void throwsExceptionWhenAddingStaticItem() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        List<Item> items = Lists.newArrayList(dummyStaticItem());
        expectedException.expect(CantOwnStaticItemException.class);

        equipmentService.addItems(items, player, response);

        assertThat(player.getEquipment().getBackpack(), hasSize(0));
    }

    @Test
    public void throwsExceptionWhenAddingNegativeNumberOfItems() throws Exception {
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        expectedException.expect(InvalidAmountException.class);

        equipmentService.addItems(ITEM_NAME, -1, player, response);
    }

    @Test
    public void reusesAddItemsMethodWithList() throws Exception {
        EquipmentService spiedEquipmentService = spy(this.equipmentService);
        Player player = dummyPlayer(Lists.newArrayList());
        PlayerResponse response = new PlayerResponse();
        Item item = dummyItem(ITEM_NAME);
        when(itemRepo.getByName(ITEM_NAME)).thenReturn(item);

        spiedEquipmentService.addItems(ITEM_NAME, 3, player, response);

        verify(spiedEquipmentService).addItems(Lists.newArrayList(item, item, item), player, response);
    }

    private void setupItemRepo() {
        setupItemRepo(dummyItem(ITEM_NAME));
    }

    private void setupItemRepo(Item item) {
        when(itemRepo.getByName(ITEM_NAME_2)).thenReturn(dummyItem(ITEM_NAME_2));
        when(itemRepo.getByName(NOT_RELEVANT_ITEM_NAME)).thenReturn(dummyItem(NOT_RELEVANT_ITEM_NAME));
        when(itemRepo.getByName(STATIC_ITEM_NAME)).thenReturn(dummyStaticItem());

        when(itemRepo.getByName(item.getName())).thenReturn(item);
    }

    private void setupScriptRepo() {
        when(scriptRepo.get(ON_DROP_SCRIPT_ID)).thenReturn(dummyScript(ON_DROP_SCRIPT_ID));
        when(scriptRepo.get(BEFORE_DROP_SCRIPT_ID)).thenReturn(dummyScript(BEFORE_DROP_SCRIPT_ID));
        when(scriptRepo.get(ON_RECEIVE_SCRIPT_ID)).thenReturn(dummyScript(ON_RECEIVE_SCRIPT_ID));
        when(scriptRepo.get(BEFORE_RECEIVE_SCRIPT_ID)).thenReturn(dummyScript(BEFORE_RECEIVE_SCRIPT_ID));
    }

    private void verifyScriptRunnerNeverCalledWith(String... ids) {
        for (String id : ids) {
            verify(scriptRunner, never()).execute(eq(dummyScript(id)), any(), any(), any(), any());
        }
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
        return dummyItemBuilder(itemName).build();
    }

    private Armor.ArmorBuilder dummyItemBuilder(String itemName) {
        return Armor.builder().name(itemName);
    }

    private List<Item> dummyItems(String... names) {
        List<Item> items = Lists.newArrayList();
        for (int i = 0; i < names.length; i++) {
            items.add(dummyItem(names[i]));
        }
        return items;
    }

    private Script dummyScript(String id) {
        return Script.builder().id(id).build();
    }

    private List<ScriptBinding> dummyBindings(Item item, int amount) {
        return Lists.newArrayList(
                ScriptBinding.builder().name("item").object(item).build(),
                ScriptBinding.builder().name("amount").object(amount).build()
        );
    }
}