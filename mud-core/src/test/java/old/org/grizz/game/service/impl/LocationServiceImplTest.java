package old.org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import old.org.grizz.game.TestContext;
import old.org.grizz.game.model.Location;
import old.org.grizz.game.model.impl.LocationEntity;
import old.org.grizz.game.model.impl.LocationItemsEntity;
import old.org.grizz.game.model.impl.PlayerContextImpl;
import old.org.grizz.game.model.impl.items.MiscEntity;
import old.org.grizz.game.model.impl.items.WeaponEntity;
import old.org.grizz.game.model.items.Item;
import old.org.grizz.game.model.repository.ItemRepo;
import old.org.grizz.game.model.repository.LocationItemsRepository;
import old.org.grizz.game.model.repository.LocationRepo;
import old.org.grizz.game.service.simple.LocationService;
import old.org.grizz.game.service.simple.impl.LocationServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Grizz on 2015-08-17.
 */
@ContextConfiguration(classes = {TestContext.class})
@RunWith(MockitoJUnitRunner.class)
public class LocationServiceImplTest {
    @InjectMocks
    private LocationService locationService = new LocationServiceImpl();

    @Mock
    private LocationRepo locationRepo;

    @Mock
    private ItemRepo itemRepo;

    @Mock
    private LocationItemsRepository locationItemsRepo;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCurrentLocation() {
        LocationEntity locationEntity = LocationEntity.builder().id("1").build();
        PlayerContextImpl context = PlayerContextImpl.builder().currentLocation(locationEntity.getId()).build();
        when(locationRepo.get(anyString())).thenReturn(locationEntity);

        Location currentLocation = locationService.getCurrentLocation(context);

        assertThat(currentLocation, equalTo(locationEntity));
    }

    @Test
    public void testAddItemsToLocation_noItemsToEmptyLocation() {
        String id = "1";
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList())
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        long itemsOnLocationBefore = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();
        List<Item> items = Lists.newArrayList();

        locationService.addItems(locationEntity, items);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsOnLocationAfter = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsOnLocationAfter - itemsOnLocationBefore)));
    }

    @Test
    public void testAddItemsToLocation_singleItemToEmptyLocation() {
        String id = "1";
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList())
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        long itemsOnLocationBefore = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();
        List<Item> items = Lists.newArrayList(MiscEntity.builder().id(id).build());

        locationService.addItems(locationEntity, items);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsOnLocationAfter = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsOnLocationAfter - itemsOnLocationBefore)));
    }

    @Test
    public void testAddItemsToLocation_singleItemToNonEmptyLocationWithDifferentItem() {
        String id = "1";
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList(MiscEntity.builder().id("2").build()))
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        long itemsOnLocationBefore = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();
        List<Item> items = Lists.newArrayList(MiscEntity.builder().id(id).build());

        locationService.addItems(locationEntity, items);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsOnLocationAfter = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsOnLocationAfter - itemsOnLocationBefore)));
    }

    @Test
    public void testAddItemsToLocation_singleItemToNonEmptyLocationWithSameItem() {
        String id = "1";
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList(MiscEntity.builder().id(id).build()))
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        long itemsOnLocationBefore = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();
        List<Item> items = Lists.newArrayList(MiscEntity.builder().id(id).build());

        locationService.addItems(locationEntity, items);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsOnLocationAfter = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsOnLocationAfter - itemsOnLocationBefore)));
    }

    @Test
    public void testAddItemsToLocation_multipleItemsToEmptyLocation() {
        String id = "1";
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList())
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        long itemsOnLocationBefore = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();
        List<Item> items = Lists.newArrayList(
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build()
        );

        locationService.addItems(locationEntity, items);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsOnLocationAfter = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsOnLocationAfter - itemsOnLocationBefore)));
    }

    @Test
    public void testAddItemsToLocation_multipleItemsToNonEmptyLocationWithSameItems() {
        String id = "1";
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList(
                                MiscEntity.builder().id(id).build(),
                                MiscEntity.builder().id(id).build(),
                                MiscEntity.builder().id(id).build()
                        ))
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        long itemsOnLocationBefore = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();
        List<Item> items = Lists.newArrayList(
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build()
        );

        locationService.addItems(locationEntity, items);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsOnLocationAfter = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsOnLocationAfter - itemsOnLocationBefore)));
    }

    @Test
    public void testAddItemsToLocation_multipleItemsToNonEmptyLocationWithDifferentItems() {
        String id = "1";
        String id2 = "2";
        String id3 = "3";
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList(
                                MiscEntity.builder().id(id3).build(),
                                MiscEntity.builder().id(id).build(),
                                MiscEntity.builder().id(id2).build(),
                                MiscEntity.builder().id(id).build(),
                                MiscEntity.builder().id(id2).build(),
                                MiscEntity.builder().id(id).build(),
                                MiscEntity.builder().id(id3).build()
                        ))
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        long itemsOnLocationBefore = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();
        List<Item> items = Lists.newArrayList(
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build(),
                MiscEntity.builder().id(id).build()
        );

        locationService.addItems(locationEntity, items);

        long itemsAdded = items.stream().filter(i -> i.getId().equals(id)).count();
        long itemsOnLocationAfter = locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(itemsAdded, is(equalTo(itemsOnLocationAfter - itemsOnLocationBefore)));
    }

    @Test
    public void testRemoveItemsFromLocation_noItemsFromEmptyLocation() {
        String id = "1";
        String itemName = "Miecz";
        int itemsRemoved = 0;

        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        when(itemRepo.getByName(itemName)).thenReturn(item);
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList())
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();

        locationService.removeItems(locationEntity, itemName, itemsRemoved);
    }

    @Test
    public void testRemoveItemsFromLocation_noItemsFromLocationWithSingleSameItem() {
        String id = "1";
        String itemName = "Miecz";
        int itemsRemoved = 0;

        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        when(itemRepo.getByName(itemName)).thenReturn(item);
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList(item))
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();

        locationService.removeItems(locationEntity, itemName, itemsRemoved);
    }

    @Test
    public void testRemoveItemsFromLocation_SingleItemFromLocationWithSingleSameItem() {
        String id = "1";
        String itemName = "Miecz";
        int amountToRemove = 1;

        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        when(itemRepo.getByName(itemName)).thenReturn(item);
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList(item))
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        int itemsOnLocationBefore = (int) locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        List<Item> itemsRemoved = locationService.removeItems(locationEntity, itemName, amountToRemove);

        int itemsOnLocationAfter = (int) locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(amountToRemove, is(equalTo(itemsRemoved.size())));
        assertThat(amountToRemove, is(equalTo(itemsOnLocationBefore - itemsOnLocationAfter)));
    }

    @Test
    public void testRemoveItemsFromLocation_SingleItemFromLocationWithMultipleSameItems() {
        String id = "1";
        String itemName = "Miecz";
        int amountToRemove = 1;

        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        when(itemRepo.getByName(itemName)).thenReturn(item);
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList(
                                item,
                                item,
                                item,
                                item,
                                item
                        ))
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        int itemsOnLocationBefore = (int) locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        List<Item> itemsRemoved = locationService.removeItems(locationEntity, itemName, amountToRemove);

        int itemsOnLocationAfter = (int) locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(amountToRemove, is(equalTo(itemsRemoved.size())));
        assertThat(amountToRemove, is(equalTo(itemsOnLocationBefore - itemsOnLocationAfter)));
    }

    @Test
    public void testRemoveItemsFromLocation_MultipleItemsFromLocationWithMultipleSameItems() {
        String id = "1";
        String itemName = "Miecz";
        int amountToRemove = 3;

        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        when(itemRepo.getByName(itemName)).thenReturn(item);
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList(
                                item,
                                item,
                                item,
                                item,
                                item
                        ))
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        int itemsOnLocationBefore = (int) locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        List<Item> itemsRemoved = locationService.removeItems(locationEntity, itemName, amountToRemove);

        int itemsOnLocationAfter = (int) locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(amountToRemove, is(equalTo(itemsRemoved.size())));
        assertThat(amountToRemove, is(equalTo(itemsOnLocationBefore - itemsOnLocationAfter)));
    }

    @Test
    public void testRemoveItemsFromLocation_MultipleItemsFromLocationWithMultipleVariousItems() {
        String id = "1";
        String itemName = "Miecz";
        int amountToRemove = 3;

        Item item = WeaponEntity.builder().id(id).name(itemName).build();
        Item item2 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        Item item3 = WeaponEntity.builder().id(RandomStringUtils.random(10)).name(RandomStringUtils.random(10)).build();
        when(itemRepo.getByName(itemName)).thenReturn(item);
        LocationEntity locationEntity = LocationEntity.builder()
                .items(LocationItemsEntity.builder()
                        .mobileItems(Lists.newArrayList(
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
                        .staticItems(Lists.newArrayList())
                        .build())
                .build();
        int itemsOnLocationBefore = (int) locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        List<Item> itemsRemoved = locationService.removeItems(locationEntity, itemName, amountToRemove);

        int itemsOnLocationAfter = (int) locationEntity.getItems().getMobileItems().stream().filter(i -> i.getId().equals(id)).count();

        assertThat(amountToRemove, is(equalTo(itemsRemoved.size())));
        assertThat(amountToRemove, is(equalTo(itemsOnLocationBefore - itemsOnLocationAfter)));
    }

    //TODO: Test exception branches in removing and adding items
}