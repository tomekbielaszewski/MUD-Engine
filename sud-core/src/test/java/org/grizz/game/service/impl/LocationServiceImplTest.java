package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.config.GameConfig;
import org.grizz.game.model.impl.LocationEntity;
import org.grizz.game.model.impl.items.ItemStackEntity;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void addItemToLocation_mergeWithExistingItemStack() throws Exception {
        LocationEntity location = getLocationWithThreeItemStacks();
        ItemStackEntity itemStack = ItemStackEntity.builder()
                .itemId("1")
                .quantity(10)
                .build();

        locationService.addItemsToLocation(location, itemStack);

        assertTrue(location.getItems().contains(ItemStackEntity.builder()
                .itemId("1")
                .quantity(20)
                .build()));
        assertTrue(location.getItems().size() == 3);
    }

    @Test
    public void addItemToLocation_createNewItemStack() throws Exception {
        LocationEntity location = getLocationWithThreeItemStacks();
        ItemStackEntity itemStack = ItemStackEntity.builder()
                .itemId("3")
                .quantity(10)
                .build();

        locationService.addItemsToLocation(location, itemStack);

        assertTrue(location.getItems().contains(ItemStackEntity.builder()
                .itemId("3")
                .quantity(10)
                .build()));
        assertTrue(location.getItems().size() == 4);
    }

    @Test
    public void addItemToLocation_addEmptyItemStackToExistingItemStack() throws Exception {
        LocationEntity location = getLocationWithThreeItemStacks();
        ItemStackEntity itemStack = ItemStackEntity.builder()
                .itemId("1")
                .quantity(0)
                .build();

        locationService.addItemsToLocation(location, itemStack);

        assertTrue(location.getItems().contains(ItemStackEntity.builder()
                .itemId("1")
                .quantity(10)
                .build()));
        assertTrue(location.getItems().size() == 3);
    }

    @Test
    public void addItemToLocation_addEmptyItemStackAsNewItemStack() throws Exception {
        LocationEntity location = getLocationWithThreeItemStacks();
        ItemStackEntity itemStack = ItemStackEntity.builder()
                .itemId("3")
                .quantity(0)
                .build();

        locationService.addItemsToLocation(location, itemStack);

        assertFalse(location.getItems().contains(ItemStackEntity.builder()
                .itemId("3")
                .quantity(0)
                .build()));
        assertTrue(location.getItems().size() == 3);
    }

    private LocationEntity getLocationWithThreeItemStacks() {
        return LocationEntity.builder()
                .items(Lists.newArrayList(
                        ItemStackEntity.builder()
                                .itemId("0")
                                .quantity(10)
                                .build(),
                        ItemStackEntity.builder()
                                .itemId("1")
                                .quantity(10)
                                .build(),
                        ItemStackEntity.builder()
                                .itemId("2")
                                .quantity(10)
                                .build()
                ))
                .build();
    }
}