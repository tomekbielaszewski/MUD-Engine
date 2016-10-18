package org.grizz.game.service;

import com.google.common.collect.Lists;
import org.grizz.game.exception.CantDropStaticItemException;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Location;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Static;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {
    private static final String ITEM_1 = "itemName 1";
    private static final String ITEM_2 = "itemName 2";
    private static final String ITEM_3 = "itemName 3";
    private static final String ITEM_4 = "itemName 4";
    private static final String ITEM_5 = "itemName 5";

    @Mock
    private LocationItemsRepository locationItemsRepository;

    @InjectMocks
    private LocationService locationService = new LocationService();

    @Test
    public void manyDroppedItemsAreAddedToEmptyLocation() throws Exception {
        Location location = dummyLocation(Lists.newArrayList());
        List<Item> items = dummyItems(ITEM_1, ITEM_2, ITEM_3);

        locationService.dropItems(items, location);

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
    public void manyDroppedItemsAreAddedToNonEmptyLocation() throws Exception {
        Location location = dummyLocation(Lists.newArrayList(dummyItems(ITEM_4, ITEM_5)));
        List<Item> items = dummyItems(ITEM_1, ITEM_2, ITEM_3);

        locationService.dropItems(items, location);

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
    public void singleDroppedItemIsAddedToEmptyLocation() throws Exception {
        Location location = dummyLocation(Lists.newArrayList());
        List<Item> items = dummyItems(ITEM_1);

        locationService.dropItems(items, location);

        assertThat(location.getItems().getMobileItems(), hasSize(1));
        assertThat(location.getItems().getMobileItems(), hasItem(dummyItem(ITEM_1)));
        assertThat(location.getItems().getStaticItems(), hasSize(0));
        verify(locationItemsRepository).save(location.getItems());
    }

    @Test
    public void singleDroppedItemIsAddedToNonEmptyLocation() throws Exception {
        Location location = dummyLocation(Lists.newArrayList(dummyItems(ITEM_4, ITEM_5)));
        List<Item> items = dummyItems(ITEM_1);

        locationService.dropItems(items, location);

        assertThat(location.getItems().getMobileItems(), hasSize(3));
        assertThat(location.getItems().getMobileItems(), hasItems(
                dummyItem(ITEM_1),
                dummyItem(ITEM_4),
                dummyItem(ITEM_5)
        ));
        assertThat(location.getItems().getStaticItems(), hasSize(0));
        verify(locationItemsRepository).save(location.getItems());
    }

    @Test(expected = InvalidAmountException.class)
    public void throwsExceptionWhenDroppingZeroItems() throws Exception {
        Location location = dummyLocation(Lists.newArrayList());
        List<Item> items = Lists.newArrayList();

        locationService.dropItems(items, location);
    }

    @Test(expected = CantDropStaticItemException.class)
    public void throwsExceptionWhenDroppingStaticItems() throws Exception {
        Location location = dummyLocation(Lists.newArrayList());
        List<Item> items = Lists.newArrayList(
                Static.builder().build()
        );

        locationService.dropItems(items, location);
    }

    @Test
    public void pickItems() throws Exception {
        throw new NotImplementedException();
    }

    private Location dummyLocation(List<Item> mobileItems) {
        return Location.builder()
                .items(LocationItems.builder()
                        .mobileItems(mobileItems)
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
    }

    private Armor dummyItem(String name) {
        return Armor.builder().name(name).build();
    }

    private List<Item> dummyItems(String... names) {
        List<Item> items = Lists.newArrayList();
        for (int i = 0; i < names.length; i++) {
            items.add(dummyItem(names[i]));
        }
        return items;
    }
}