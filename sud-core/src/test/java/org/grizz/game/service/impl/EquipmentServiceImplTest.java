package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.grizz.game.config.GameConfig;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.EquipmentEntity;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.impl.items.WeaponEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.service.simple.EventService;
import org.grizz.game.service.simple.impl.EquipmentServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by tomasz.bielaszewski on 2015-10-05.
 */
@ContextConfiguration(classes = {GameConfig.class})
@RunWith(MockitoJUnitRunner.class)
public class EquipmentServiceImplTest {
    @InjectMocks
    private EquipmentService equipmentService = new EquipmentServiceImpl();

    @Mock
    private EventService eventService;

    @Mock
    private ItemRepo itemRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddItems_NoneItemsToEmptyBackpack() {
        String id = "1";
        List<Item> items = Lists.newArrayList();

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList())
                        .build())
                .build();
        long itemsInBackpackBefore = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();
        PlayerResponse response = new PlayerResponseImpl();

        equipmentService.addItems(items, context, response);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsInBackpackAfter = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsInBackpackAfter - itemsInBackpackBefore)));
    }

    @Test
    public void testAddItems_SingleItemToEmptyBackpack() {
        String id = "1";
        String itemName = "Miecz";
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        List<Item> items = Lists.newArrayList(item);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList())
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        long itemsInBackpackBefore = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        equipmentService.addItems(items, context, response);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsInBackpackAfter = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsInBackpackAfter - itemsInBackpackBefore)));
    }

    @Test
    public void testAddItems_SingleItemToNonEmptyBackpackWithSameItem() {
        String id = "1";
        String itemName = "Miecz";
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        List<Item> items = Lists.newArrayList(item);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(item))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        long itemsInBackpackBefore = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        equipmentService.addItems(items, context, response);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsInBackpackAfter = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsInBackpackAfter - itemsInBackpackBefore)));
    }

    @Test
    public void testAddItems_SingleItemToNonEmptyBackpackWithManySameItems() {
        String id = "1";
        String itemName = "Miecz";
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        List<Item> items = Lists.newArrayList(item);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                item,
                                item,
                                item,
                                item,
                                item
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        long itemsInBackpackBefore = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        equipmentService.addItems(items, context, response);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsInBackpackAfter = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsInBackpackAfter - itemsInBackpackBefore)));
    }

    @Test
    public void testAddItems_SingleItemToNonEmptyBackpackWithManyVariousItems() {
        String id = "1";
        String itemName = "Miecz";
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        Item item2 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        Item item3 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        List<Item> items = Lists.newArrayList(item);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                item3,
                                item,
                                item,
                                item2,
                                item,
                                item2,
                                item,
                                item,
                                item3,
                                item3
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        long itemsInBackpackBefore = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        equipmentService.addItems(items, context, response);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsInBackpackAfter = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsInBackpackAfter - itemsInBackpackBefore)));
    }

    @Test
    public void testAddItems_ManyItemsToEmptyBackpack() {
        String id = "1";
        String itemName = "Miecz";
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        List<Item> items = Lists.newArrayList(
                item,
                item,
                item,
                item,
                item
        );

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList())
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        long itemsInBackpackBefore = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        equipmentService.addItems(items, context, response);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsInBackpackAfter = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsInBackpackAfter - itemsInBackpackBefore)));
    }

    @Test
    public void testAddItems_ManyItemsToNonEmptyBackpackWithManySameItems() {
        String id = "1";
        String itemName = "Miecz";
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        List<Item> items = Lists.newArrayList(
                item,
                item,
                item,
                item,
                item
        );

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                item,
                                item,
                                item,
                                item,
                                item
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        long itemsInBackpackBefore = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        equipmentService.addItems(items, context, response);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsInBackpackAfter = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsInBackpackAfter - itemsInBackpackBefore)));
    }

    @Test
    public void testAddItems_ManyItemsToNonEmptyBackpackWithManyVariousItems() {
        String id = "1";
        String itemName = "Miecz";
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        Item item2 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        Item item3 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        List<Item> items = Lists.newArrayList(
                item,
                item,
                item,
                item,
                item
        );

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                item3,
                                item,
                                item,
                                item2,
                                item,
                                item2,
                                item,
                                item,
                                item3,
                                item3
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        long itemsInBackpackBefore = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        equipmentService.addItems(items, context, response);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsInBackpackAfter = context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsInBackpackAfter - itemsInBackpackBefore)));
    }

    @Test
    public void testRemoveItems_RemovingNoneItemsFromEmptyBackpack() {
        String id = "1";
        String itemName = "Miecz";
        int amount = 0;

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList())
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        int itemsInBackpackBefore = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        List<Item> removedItems = equipmentService.removeItems(itemName, amount, context, response);

        int itemsInBackpackAfter = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(removedItems.size(), is(equalTo(amount)));
        assertThat(amount, is(equalTo(itemsInBackpackBefore - itemsInBackpackAfter)));
    }

    @Test
    public void testRemoveItems_RemovingSingleItemFromBackpackWithOneItem() {
        String id = "1";
        String itemName = "Miecz";
        int amount = 1;
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        when(itemRepo.getByName(itemName)).thenReturn(item);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(item))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        int itemsInBackpackBefore = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        List<Item> removedItems = equipmentService.removeItems(itemName, amount, context, response);

        int itemsInBackpackAfter = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(removedItems.size(), is(equalTo(amount)));
        assertThat(amount, is(equalTo(itemsInBackpackBefore - itemsInBackpackAfter)));
    }

    @Test
    public void testRemoveItems_RemovingSingleItemFromBackpackWithManySameItems() {
        String id = "1";
        String itemName = "Miecz";
        int amount = 1;
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        when(itemRepo.getByName(itemName)).thenReturn(item);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                item,
                                item,
                                item,
                                item,
                                item
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        int itemsInBackpackBefore = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        List<Item> removedItems = equipmentService.removeItems(itemName, amount, context, response);

        int itemsInBackpackAfter = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(removedItems.size(), is(equalTo(amount)));
        assertThat(amount, is(equalTo(itemsInBackpackBefore - itemsInBackpackAfter)));
    }

    @Test
    public void testRemoveItems_RemovingSingleItemFromBackpackWithManyVariousItems() {
        String id = "1";
        String itemName = "Miecz";
        int amount = 1;
        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        Item item2 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        Item item3 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        when(itemRepo.getByName(itemName)).thenReturn(item);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                item,
                                item,
                                item2,
                                item3,
                                item,
                                item2,
                                item,
                                item3,
                                item3,
                                item,
                                item2
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        int itemsInBackpackBefore = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        List<Item> removedItems = equipmentService.removeItems(itemName, amount, context, response);

        int itemsInBackpackAfter = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(removedItems.size(), is(equalTo(amount)));
        assertThat(amount, is(equalTo(itemsInBackpackBefore - itemsInBackpackAfter)));
    }

    @Test
    public void testRemoveItems_RemovingManyItemsFromBackpackWithManyVariousItems() {
        String id = "1";
        String itemName = "Miecz";
        int amount = 3;
        Item itemToRemove = WeaponEntity.builder().id(id).name(itemName).build();
        Item item2 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        Item item3 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        when(itemRepo.getByName(itemName)).thenReturn(itemToRemove);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                itemToRemove,
                                itemToRemove,
                                item2,
                                item3,
                                itemToRemove,
                                item2,
                                itemToRemove,
                                item3,
                                item3,
                                itemToRemove,
                                item2
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();
        int itemsInBackpackBefore = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        List<Item> removedItems = equipmentService.removeItems(itemName, amount, context, response);

        int itemsInBackpackAfter = (int) context.getEquipment().getBackpack().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(removedItems.size(), is(equalTo(amount)));
        assertThat(amount, is(equalTo(itemsInBackpackBefore - itemsInBackpackAfter)));
    }

    @Test(expected = NoSuchItemException.class)
    public void testRemoveItems_RemovingSingleItemFromBackpackWithManyVariousItemsButWithoutItemWeWantToRemove() {
        String id = "1";
        String itemName = "Miecz";
        int amount = 1;
        Item itemToRemove = WeaponEntity.builder().id(id).name(itemName).build();
        Item item1 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        Item item2 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        Item item3 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        when(itemRepo.getByName(itemName)).thenReturn(itemToRemove);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                item1,
                                item1,
                                item2,
                                item3,
                                item1,
                                item2,
                                item1,
                                item3,
                                item3,
                                item1,
                                item2
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();

        equipmentService.removeItems(itemName, amount, context, response);
    }

    @Test(expected = NotEnoughItemsException.class)
    public void testRemoveItems_RemovingManyItemsFromBackpackWithManyVariousItemsButNotEnoughItemsWeWantToRemove() {
        String id = "1";
        String itemName = "Miecz";
        int amount = 3;
        Item itemToRemove = WeaponEntity.builder().id(id).name(itemName).build();
        Item item1 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        Item item2 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        Item item3 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        when(itemRepo.getByName(itemName)).thenReturn(itemToRemove);

        PlayerContext context = PlayerContextImpl.builder()
                .equipment(EquipmentEntity.builder()
                        .backpack(Lists.newArrayList(
                                item1,
                                item1,
                                item2,
                                item3,
                                itemToRemove,
                                item1,
                                item2,
                                item1,
                                itemToRemove,
                                item3,
                                item3,
                                item1,
                                item2
                        ))
                        .build())
                .build();
        PlayerResponse response = new PlayerResponseImpl();

        equipmentService.removeItems(itemName, amount, context, response);
    }

    //TODO: Refactor tests - move all repeating code to single method
}