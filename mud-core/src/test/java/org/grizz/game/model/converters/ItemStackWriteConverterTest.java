package org.grizz.game.model.converters;

import com.mongodb.DBObject;
import org.grizz.game.model.ItemStack;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ItemStackWriteConverterTest {
    private static final String ID = "id";
    private static final int AMOUNT = 10;
    private static final String NAME = "name";

    private ItemStackWriteConverter converter = new ItemStackWriteConverter();

    @Test
    public void convertItemStack() throws Exception {
        ItemStack itemStack = dummyItemStack(ID, AMOUNT, NAME);

        DBObject converted = converter.convert(itemStack);

        assertThat(converted.get("id"), is(ID));
        assertThat(converted.get("name"), is(NAME));
        assertThat(converted.get("amount"), is(AMOUNT));
    }

    private ItemStack dummyItemStack(String id, int amount, String name) {
        return ItemStack.builder()
                .id(id)
                .amount(amount)
                .name(name)
                .build();
    }
}