package org.grizz.game.model.converters;

import com.mongodb.DBObject;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ItemWriteConverterTest {
    private final String ID = "id";
    private final String NAME = "name";

    private ItemWriteConverter converter = new ItemWriteConverter();

    @Test
    public void convertItem() throws Exception {
        Item item = dummyItem(ID, NAME);

        DBObject converted = converter.convert(item);

        assertThat(converted.get("_id"), is(ID));
        assertThat(converted.get("name"), is(NAME));
    }

    private Item dummyItem(String id, String name) {
        return Armor.builder()
                .id(id)
                .name(name)
                .build();
    }
}