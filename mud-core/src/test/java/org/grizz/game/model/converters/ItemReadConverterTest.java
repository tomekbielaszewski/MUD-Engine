package org.grizz.game.model.converters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ItemReadConverterTest {
    private static final String ID = "id";

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private ItemReadConverter converter = new ItemReadConverter();

    @Test
    public void shouldGetItemByIdFromDBObject() throws Exception {
        Item expectedItem = dummyItem(ID);
        Mockito.when(itemRepo.get(ID)).thenReturn(expectedItem);
        DBObject dbo = new BasicDBObject();
        dbo.put("_id", ID);

        Item converted = converter.convert(dbo);

        assertThat(converted.getId(), is(ID));
    }

    private Item dummyItem(String id) {
        return Armor.builder()
                .id(id)
                .build();
    }
}