package org.grizz.game.config;

import com.mongodb.DBObject;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.Location;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.converters.*;
import org.grizz.game.model.items.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MongoConfigTest {
    private MongoConfig mongoConfig = new MongoConfig();

    @Test
    public void shouldRegisterAllWriteConvertersProperly() throws Exception {
        CustomConversions customConversions = mongoConfig.customConversions(
          new ItemReadConverter(),
          new ItemWriteConverter(),
          new LocationItemsReadConverter(),
          new LocationItemsWriteConverter(),
          new EquipmentReadConverter(),
          new EquipmentWriteConverter()
        );

        Class itemConversionTarget = customConversions.getCustomWriteTarget(Item.class);
        Class locationItemsConversionTarget = customConversions.getCustomWriteTarget(LocationItems.class);
        Class equipmentConversionTarget = customConversions.getCustomWriteTarget(Equipment.class);
        Class nonExistingConversionTarget = customConversions.getCustomWriteTarget(Location.class);

        assertTrue(itemConversionTarget.equals(DBObject.class));
        assertTrue(locationItemsConversionTarget.equals(DBObject.class));
        assertTrue(equipmentConversionTarget.equals(DBObject.class));
        assertThat(nonExistingConversionTarget, is(nullValue()));
    }
}