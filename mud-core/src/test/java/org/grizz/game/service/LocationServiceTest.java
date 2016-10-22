package org.grizz.game.service;

import com.google.common.collect.Lists;
import old.org.grizz.game.exception.NoSuchItemException;
import old.org.grizz.game.exception.NotEnoughItemsException;
import org.grizz.game.exception.CantMoveStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Static;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {
    private static final String ITEM_1 = "itemName 1";
    private static final String ITEM_2 = "itemName 2";
    private static final String ITEM_3 = "itemName 3";
    private static final String ITEM_4 = "itemName 4";
    private static final String ITEM_5 = "itemName 5";

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Mock
    private LocationItemsRepository locationItemsRepository;
    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private LocationService locationService = new LocationService();

    @Test
    public void manyItemsAreAddedToEmptyLocation() throws Exception {
        Location location = dummyLocation(Lists.newArrayList());
        List<Item> items = dummyItems(ITEM_1, ITEM_2, ITEM_3);

        locationService.addItems(items, location);

        assertThat(location.getItems().getMobileItems(), hasSize(3));
        assertThat(location.getItems().getMobileItems(), hasItems(
                dummyItem(ITEM_1),
                dummyItem(ITEM_2),
                dummyItem(ITEM_3)
        ));
        assertThat(location.getItems().getStaticItems(), hasSize(0));
        verify(locationItemsRepository).save(location.getItems());
    }

    @Test
    public void manyItemsAreAddedToNonEmptyLocation() throws Exception {
        Location location = dummyLocation(Lists.newArrayList(dummyItems(ITEM_4, ITEM_5)));
        List<Item> items = dummyItems(ITEM_1, ITEM_2, ITEM_3);

        locationService.addItems(items, location);

        assertThat(location.getItems().getMobileItems(), hasSize(5));
        assertThat(location.getItems().getMobileItems(), hasItems(
                dummyItem(ITEM_1),
                dummyItem(ITEM_2),
                dummyItem(ITEM_3),
                dummyItem(ITEM_4),
                dummyItem(ITEM_5)
        ));
        assertThat(location.getItems().getStaticItems(), hasSize(0));
        verify(locationItemsRepository).save(location.getItems());
    }

    @Test
    public void singleItemIsAddedToEmptyLocation() throws Exception {
        Location location = dummyLocation(Lists.newArrayList());
        List<Item> items = dummyItems(ITEM_1);

        locationService.addItems(items, location);

        assertThat(location.getItems().getMobileItems(), hasSize(1));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_1)));
        assertThat(location.getItems().getStaticItems(), hasSize(0));
        verify(locationItemsRepository).save(location.getItems());
    }

    @Test
    public void singleItemIsAddedToNonEmptyLocation() throws Exception {
        Location location = dummyLocation(Lists.newArrayList(dummyItems(ITEM_4, ITEM_5)));
        List<Item> items = dummyItems(ITEM_1);

        locationService.addItems(items, location);

        assertThat(location.getItems().getMobileItems(), hasSize(3));
        assertThat(location.getItems().getMobileItems(), hasItems(
                dummyItem(ITEM_1),
                dummyItem(ITEM_4),
                dummyItem(ITEM_5)
        ));
        assertThat(location.getItems().getStaticItems(), hasSize(0));
        verify(locationItemsRepository).save(location.getItems());
    }

    @Test
    public void throwsExceptionWhenAddingZeroItems() throws Exception {
        Location location = dummyLocation(Lists.newArrayList());
        List<Item> items = Lists.newArrayList();
        expectedException.expect(InvalidAmountException.class);

        locationService.addItems(items, location);
    }

    @Test
    public void throwsExceptionWhenAddingStaticItems() throws Exception {
        Location location = dummyLocation(Lists.newArrayList());
        List<Item> items = Lists.newArrayList(
                Static.builder().build()
        );
        expectedException.expect(CantMoveStaticItemException.class);

        locationService.addItems(items, location);
    }

    @Test
    public void removesSingleExistingItem() throws Exception {
        List<Item> itemsOnLocation = dummyItems(ITEM_1);
        Location location = dummyLocation(itemsOnLocation);
        when(itemRepo.getByName(ITEM_1)).thenReturn(dummyItem(ITEM_1));

        List<Item> removedItems = locationService.removeItems(ITEM_1, 1, location);

        assertThat(removedItems, hasSize(1));
        assertThat(removedItems, hasItem(dummyItem(ITEM_1)));
        assertThat(location.getItems().getMobileItems(), hasSize(0));
        assertThat(location.getItems().getStaticItems(), hasSize(0));
        verify(locationItemsRepository).save(location.getItems());
    }

    @Test
    public void removesMostOfExistingItems() throws Exception {
        List<Item> itemsOnLocation = dummyItems(ITEM_1, ITEM_1, ITEM_1);
        Location location = dummyLocation(itemsOnLocation);
        when(itemRepo.getByName(ITEM_1)).thenReturn(dummyItem(ITEM_1));

        List<Item> removedItems = locationService.removeItems(ITEM_1, 2, location);

        assertThat(removedItems, hasSize(2));
        assertThat(removedItems, hasItem(dummyItem(ITEM_1)));
        assertThat(location.getItems().getMobileItems(), hasSize(1));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_1)));
        assertThat(location.getItems().getStaticItems(), hasSize(0));
        verify(locationItemsRepository).save(location.getItems());
    }

    @Test
    public void removesMostOfExistingItemsWhenOtherItemsAlsoExist() throws Exception {
        List<Item> itemsOnLocation = dummyItems(ITEM_1, ITEM_1, ITEM_1, ITEM_2, ITEM_3, ITEM_2, ITEM_1);
        Location location = dummyLocation(itemsOnLocation);
        when(itemRepo.getByName(ITEM_1)).thenReturn(dummyItem(ITEM_1));

        List<Item> removedItems = locationService.removeItems(ITEM_1, 3, location);

        assertThat(removedItems, hasSize(3));
        assertThat(removedItems, hasItem(dummyItem(ITEM_1)));
        assertThat(removedItems, not(hasItem(dummyItem(ITEM_2))));
        assertThat(removedItems, not(hasItem(dummyItem(ITEM_3))));
        assertThat(location.getItems().getMobileItems(), hasSize(4));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_1)));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_2)));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_3)));
        assertThat(location.getItems().getStaticItems(), hasSize(0));
        verify(locationItemsRepository).save(location.getItems());
    }

    @Test
    public void throwsExceptionWhenRemovingTooManyItems() throws Exception {
        List<Item> itemsOnLocation = dummyItems(ITEM_1);
        Location location = dummyLocation(itemsOnLocation);
        when(itemRepo.getByName(ITEM_1)).thenReturn(dummyItem(ITEM_1));
        expectedException.expect(NotEnoughItemsException.class);

        locationService.removeItems(ITEM_1, 2, location);

        assertThat(location.getItems().getMobileItems(), hasSize(1));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_1)));
        verify(locationItemsRepository, never()).save(location.getItems());
    }

    @Test
    public void throwsExceptionWhenRemovingNonExistingItem() throws Exception {
        List<Item> itemsOnLocation = dummyItems(ITEM_2);
        Location location = dummyLocation(itemsOnLocation);
        when(itemRepo.getByName(ITEM_1)).thenReturn(dummyItem(ITEM_1));
        expectedException.expect(NoSuchItemException.class);

        locationService.removeItems(ITEM_1, 1, location);

        assertThat(location.getItems().getMobileItems(), hasSize(1));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_2)));
        verify(locationItemsRepository, never()).save(location.getItems());
    }

    @Test
    public void throwsExceptionWhenRemovingStaticItem() throws Exception {
        List<Item> itemsOnLocation = dummyItems();
        Location location = dummyLocation(itemsOnLocation);
        location.getItems().setStaticItems(Lists.newArrayList(dummyStatic(ITEM_1)));
        when(itemRepo.getByName(ITEM_1)).thenReturn(dummyStatic(ITEM_1));
        expectedException.expect(CantMoveStaticItemException.class);

        locationService.removeItems(ITEM_1, 1, location);

        assertThat(location.getItems().getMobileItems(), hasSize(0));
        assertThat(location.getItems().getStaticItems(), hasSize(1));
        verify(locationItemsRepository, never()).save(location.getItems());
    }

    @Test
    public void throwsExceptionWhenRemovingZeroItems() throws Exception {
        List<Item> itemsOnLocation = dummyItems(ITEM_1);
        Location location = dummyLocation(itemsOnLocation);
        when(itemRepo.getByName(ITEM_1)).thenReturn(dummyItem(ITEM_1));
        expectedException.expect(InvalidAmountException.class);

        locationService.removeItems(ITEM_1, 0, location);

        assertThat(location.getItems().getMobileItems(), hasSize(1));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_1)));
        verify(locationItemsRepository, never()).save(location.getItems());
    }

    @Test
    public void throwsExceptionWhenRemovingNegativeNumberOfItems() throws Exception {
        List<Item> itemsOnLocation = dummyItems(ITEM_1);
        Location location = dummyLocation(itemsOnLocation);
        when(itemRepo.getByName(ITEM_1)).thenReturn(dummyItem(ITEM_1));
        expectedException.expect(InvalidAmountException.class);

        locationService.removeItems(ITEM_1, -1, location);

        assertThat(location.getItems().getMobileItems(), hasSize(1));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_1)));
        verify(locationItemsRepository, never()).save(location.getItems());
    }

    private Location dummyLocation(List<Item> mobileItems) {
        return Location.builder()
                .items(LocationItems.builder()
                        .mobileItems(mobileItems)
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
    }

    private Item dummyItem(String name) {
        return Armor.builder().name(name).build();
    }

    private List<Item> dummyItems(String... names) {
        List<Item> items = Lists.newArrayList();
        for (int i = 0; i < names.length; i++) {
            items.add(dummyItem(names[i]));
        }
        return items;
    }

    private Static dummyStatic(String name) {
        return Static.builder().name(name).build();
    }
}