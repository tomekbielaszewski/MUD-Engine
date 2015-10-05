package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.config.GameConfig;
import org.grizz.game.model.Location;
import org.grizz.game.model.impl.LocationEntity;
import org.grizz.game.model.impl.PlayerContextImpl;
import org.grizz.game.model.impl.items.MiscEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.LocationRepo;
import org.grizz.game.service.simple.LocationService;
import org.grizz.game.service.simple.impl.LocationServiceImpl;
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
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Grizz on 2015-08-17.
 */
@ContextConfiguration(classes = {GameConfig.class})
@RunWith(MockitoJUnitRunner.class)
public class LocationServiceImplTest {
    @InjectMocks
    private LocationService locationService = new LocationServiceImpl();

    @Mock
    private LocationRepo locationRepo;
    @Mock
    private ItemRepo itemRepo;

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
    public void testAddItemsToLocation_singleItemToEmptyLocation() {
        LocationEntity locationEntity = LocationEntity.builder().build();
        List<Item> items = Lists.newArrayList(MiscEntity.builder().id("1").build());

        locationService.addItemsToLocation(locationEntity, items);

        assertThat(locationEntity.getItems(), hasItems(items));
    }

    //TODO: Napisac od nowa testy do tego
//    @Test
//    public void addItemToLocation_createNewItemStack() throws Exception {
//        LocationEntity location = getLocationWithThreeUniqueItems_2PiecesEach();
//        ItemStackEntity itemStack = ItemStackEntity.builder()
//                .itemId("3")
//                .quantity(10)
//                .build();
//
//        locationService.addItemsToLocation(location, itemStack);
//
//        assertTrue(location.getItems().contains(ItemStackEntity.builder()
//                .itemId("3")
//                .quantity(10)
//                .build()));
//        assertTrue(location.getItems().size() == 4);
//    }
//
//    @Test
//    public void addItemToLocation_addEmptyItemStackToExistingItemStack() throws Exception {
//        LocationEntity location = getLocationWithThreeItemStacks();
//        ItemStackEntity itemStack = ItemStackEntity.builder()
//                .itemId("1")
//                .quantity(0)
//                .build();
//
//        locationService.addItemsToLocation(location, itemStack);
//
//        assertTrue(location.getItems().contains(ItemStackEntity.builder()
//                .itemId("1")
//                .quantity(10)
//                .build()));
//        assertTrue(location.getItems().size() == 3);
//    }
//
//    @Test
//    public void addItemToLocation_addEmptyItemStackAsNewItemStack() throws Exception {
//        LocationEntity location = getLocationWithThreeItemStacks();
//        ItemStackEntity itemStack = ItemStackEntity.builder()
//                .itemId("3")
//                .quantity(0)
//                .build();
//
//        locationService.addItemsToLocation(location, itemStack);
//
//        assertFalse(location.getItems().contains(ItemStackEntity.builder()
//                .itemId("3")
//                .quantity(0)
//                .build()));
//        assertTrue(location.getItems().size() == 3);
//    }

    private LocationEntity getLocationWithThreeUniqueItems_2PiecesEach() {
        return LocationEntity.builder()
                .items(Lists.newArrayList(
                        itemRepo.get("0"),
                        itemRepo.get("0"),
                        itemRepo.get("1"),
                        itemRepo.get("1"),
                        itemRepo.get("2"),
                        itemRepo.get("2")
                ))
                .build();
    }
}