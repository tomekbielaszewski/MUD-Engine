package org.grizz.game.model.converters;

import com.google.common.collect.Lists;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ItemListToCountedItemsConverterTest {
    private static final String ID_1 = "id1";
    private static final String ID_2 = "id2";
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";

    private ItemListToCountedItemsConverter converter = new ItemListToCountedItemsConverter();

    @Test
    public void countsSingleItem() throws Exception {
        Item item = dummyItem(ID_1, NAME_1);

        Map<Item, Integer> countedItems = converter.convert(Lists.newArrayList(item));

        assertThat(countedItems.entrySet(), hasSize(1));
        assertThat(countedItems.get(item), is(1));
    }

    @Test
    public void countsTwoDifferentSingleItems() throws Exception {
        Item item1 = dummyItem(ID_1, NAME_1);
        Item item2 = dummyItem(ID_2, NAME_2);

        Map<Item, Integer> countedItems = converter.convert(Lists.newArrayList(item1, item2));

        assertThat(countedItems.entrySet(), hasSize(2));
        assertThat(countedItems.get(item1), is(1));
        assertThat(countedItems.get(item2), is(1));
    }

    @Test
    public void countsTwoSameItems() throws Exception {
        Item item = dummyItem(ID_1, NAME_1);

        Map<Item, Integer> countedItems = converter.convert(Lists.newArrayList(item, item));

        assertThat(countedItems.entrySet(), hasSize(1));
        assertThat(countedItems.get(item), is(2));
    }

    @Test
    public void CountsTwoDifferentItemsWithAmountOfTwoEach() throws Exception {
        Item item1 = dummyItem(ID_1, NAME_1);
        Item item2 = dummyItem(ID_2, NAME_2);

        Map<Item, Integer> countedItems = converter.convert(Lists.newArrayList(item1, item2, item1, item2));

        assertThat(countedItems.entrySet(), hasSize(2));
        assertThat(countedItems.get(item1), is(2));
        assertThat(countedItems.get(item2), is(2));
    }

    @Test
    public void passedEmptyItemList() throws Exception {
        Map<Item, Integer> countedItems = converter.convert(Lists.newArrayList());

        assertThat(countedItems.entrySet(), is(empty()));
    }

    private Item dummyItem(String id, String name) {
        return Armor.builder()
                .id(id)
                .name(name)
                .build();
    }
}