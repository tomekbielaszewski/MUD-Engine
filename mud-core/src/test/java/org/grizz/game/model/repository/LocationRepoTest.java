package org.grizz.game.model.repository;

import old.org.grizz.game.exception.NoSuchItemException;
import old.org.grizz.game.exception.NoSuchLocationException;
import old.org.grizz.game.model.Location;
import old.org.grizz.game.model.impl.LocationEntity;
import old.org.grizz.game.model.impl.items.MiscEntity;
import old.org.grizz.game.model.items.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LocationRepoTest {
    private static final String LOCATION_ID_1 = "location 1 id";
    private static final String LOCATION_ID_2 = "location 2 id";

    @InjectMocks
    private LocationRepo locationRepo = new LocationRepo();

    @Test
    public void addAndGetLocation() throws Exception {
        Location location = dummyLocation(LOCATION_ID_1);

        locationRepo.add(location);
        Location result = locationRepo.get(LOCATION_ID_1);

        assertThat(result, is(equalTo(location)));
    }

    @Test
    public void addTwoDifferentLocationsAndGetTheFirstOne() throws Exception {
        Location expected = dummyLocation(LOCATION_ID_1);
        Location notExpected = dummyLocation(LOCATION_ID_2);

        locationRepo.add(expected);
        locationRepo.add(notExpected);
        Location result = locationRepo.get(LOCATION_ID_1);

        assertThat(result, is(equalTo(expected)));
        assertThat(result, is(not(equalTo(notExpected))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addSameLocationTwice() throws Exception {
        Location location = dummyLocation(LOCATION_ID_1);

        locationRepo.add(location);
        locationRepo.add(location);
    }

    @Test(expected = NoSuchLocationException.class)
    public void getNotExistingLocationWhenRepoIsEmpty() throws Exception {
        locationRepo.get(LOCATION_ID_1);
    }

    @Test(expected = NoSuchLocationException.class)
    public void getNotExistingLocationWhenRepoIsNotEmpty() throws Exception {
        Location location = dummyLocation(LOCATION_ID_1);

        locationRepo.add(location);
        locationRepo.get(LOCATION_ID_2);
    }

    private Location dummyLocation(String id) {
        return LocationEntity.builder()
                .id(id)
                .build();
    }
}