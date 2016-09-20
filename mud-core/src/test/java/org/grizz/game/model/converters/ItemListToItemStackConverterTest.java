package org.grizz.game.model.converters;

import com.google.common.collect.Lists;
import org.grizz.game.model.ItemStack;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ItemListToItemStackConverterTest {
    private final String ID_1 = "id1";
    private final String ID_2 = "id2";
    private final String NAME_1 = "name1";
    private final String NAME_2 = "name2";

    private ItemListToItemStackConverter converter = new ItemListToItemStackConverter();

    @Test
    public void createsSingleStackWithSingleItem() throws Exception {
        Item item = dummyItem(ID_1, NAME_1);

        List<ItemStack> itemStacks = converter.convert(Lists.newArrayList(item));

        assertThat(itemStacks, hasSize(1));
        assertThat(itemStacks, everyItem(hasProperty("amount", equalTo(1))));
        assertThat(itemStacks, everyItem(hasProperty("id", equalTo(ID_1))));
        assertThat(itemStacks, everyItem(hasProperty("name", equalTo(NAME_1))));
    }

    @Test
    public void createsTwoStacksWithSingleItems() throws Exception {
        Item item1 = dummyItem(ID_1, NAME_1);
        Item item2 = dummyItem(ID_2, NAME_2);

        List<ItemStack> itemStacks = converter.convert(Lists.newArrayList(item1, item2));

        assertThat(itemStacks, hasSize(2));
        assertThat(itemStacks, everyItem(hasProperty("amount", equalTo(1))));
        assertThat(itemStacks, hasItem(hasProperty("id", equalTo(ID_1))));
        assertThat(itemStacks, hasItem(hasProperty("name", equalTo(NAME_1))));
        assertThat(itemStacks, hasItem(hasProperty("id", equalTo(ID_2))));
        assertThat(itemStacks, hasItem(hasProperty("name", equalTo(NAME_2))));
    }

    @Test
    public void createsSingleStackWithTwoItems() throws Exception {
        Item item = dummyItem(ID_1, NAME_1);

        List<ItemStack> itemStacks = converter.convert(Lists.newArrayList(item, item));

        assertThat(itemStacks, hasSize(1));
        assertThat(itemStacks, everyItem(hasProperty("amount", equalTo(2))));
        assertThat(itemStacks, everyItem(hasProperty("id", equalTo(ID_1))));
        assertThat(itemStacks, everyItem(hasProperty("name", equalTo(NAME_1))));
    }

    @Test
    public void createsTwoStacksWithTwoItemsEach() throws Exception {
        Item item1 = dummyItem(ID_1, NAME_1);
        Item item2 = dummyItem(ID_2, NAME_2);

        List<ItemStack> itemStacks = converter.convert(Lists.newArrayList(item1, item2, item1, item2));

        assertThat(itemStacks, hasSize(2));
        assertThat(itemStacks, everyItem(hasProperty("amount", equalTo(2))));
        assertThat(itemStacks, hasItem(hasProperty("id", equalTo(ID_1))));
        assertThat(itemStacks, hasItem(hasProperty("name", equalTo(NAME_1))));
        assertThat(itemStacks, hasItem(hasProperty("id", equalTo(ID_2))));
        assertThat(itemStacks, hasItem(hasProperty("name", equalTo(NAME_2))));
    }

    @Test
    public void passedEmptyItemList() throws Exception {
        List<ItemStack> itemStacks = converter.convert(Lists.newArrayList());

        assertThat(itemStacks, hasSize(0));
    }

    private Item dummyItem(String id, String name) {
        return Armor.builder()
                .id(id)
                .name(name)
                .build();
    }
}