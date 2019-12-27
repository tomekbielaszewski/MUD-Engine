package org.grizz.game.model.mappers;

import com.google.common.collect.Lists;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.repository.ItemListRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.ResultSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LocationItemsMapperTest {
    private static final String LOCATION_ID = "location id";
    private static final String MOBILE_ITEM_ID = "mobile item id";
    private static final String STATIC_ITEM_ID = "static item id";

    @Mock
    private ItemListRepository itemListRepository;

    @InjectMocks
    private LocationItemsMapper mapper = new LocationItemsMapper();

    @Test
    public void convertsBasicLocationInfoAndItemsOnLocation() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getString("location_id")).thenReturn(LOCATION_ID);
        when(resultSet.getString("item_list_id")).thenReturn(MOBILE_ITEM_ID);
        when(resultSet.getString("static_item_list_id")).thenReturn(STATIC_ITEM_ID);

        when(itemListRepository.get(MOBILE_ITEM_ID)).thenReturn(Lists.newArrayList());
        when(itemListRepository.get(STATIC_ITEM_ID)).thenReturn(Lists.newArrayList());

        LocationItems converted = mapper.map(resultSet, null);

        assertThat(converted.getLocationId(), is(LOCATION_ID));
        verify(itemListRepository).get(MOBILE_ITEM_ID);
        verify(itemListRepository).get(STATIC_ITEM_ID);
        verifyNoMoreInteractions(itemListRepository);
    }
}
