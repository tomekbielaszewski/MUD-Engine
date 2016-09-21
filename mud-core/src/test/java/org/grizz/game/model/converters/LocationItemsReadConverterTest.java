package org.grizz.game.model.converters;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.grizz.game.model.LocationItems;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class LocationItemsReadConverterTest {
    private static final String ID = "id";
    private static final String LOCATION_ID = "location id";
    private static final String MOBILE_ITEM_ID = "mobile item id";
    private static final String STATIC_ITEM_ID = "static item id";

    @Mock
    private DBItemPackToItemListConverter itemListConverter;

    @InjectMocks
    private LocationItemsReadConverter converter = new LocationItemsReadConverter();

    @Test
    public void convertsBasicLocationInfoAndItemsOnLocation() throws Exception {
        List<DBObject> staticItems = Lists.newArrayList(new BasicDBObject("_id", STATIC_ITEM_ID));
        List<DBObject> mobileItems = Lists.newArrayList(new BasicDBObject("_id", MOBILE_ITEM_ID));
        DBObject dbObject = new BasicDBObject();
        dbObject.put("_id", ID);
        dbObject.put("locationId", LOCATION_ID);
        dbObject.put("staticItems", staticItems);
        dbObject.put("mobileItems", mobileItems);

        LocationItems converted = converter.convert(dbObject);

        assertThat(converted.getId(), is(ID));
        assertThat(converted.getLocationId(), is(LOCATION_ID));
        verify(itemListConverter).convert(mobileItems);
        verify(itemListConverter).convert(staticItems);
        verifyNoMoreInteractions(itemListConverter);
    }
}