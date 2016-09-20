package org.grizz.game.model.repository;

import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.Misc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ItemRepoTest {
    private static final String ITEM_ID_1 = "item 1 id";
    private static final String ITEM_NAME_1 = "item 1 name";
    private static final String ITEM_ID_2 = "item 2 id";
    private static final String ITEM_NAME_2 = "item 2 name";
    private static final String ITEM_NAME_WITH_ACCENTS = "ąęćłóżźĄĘĆŁÓŻŹ";

    @InjectMocks
    private ItemRepo itemRepo = new ItemRepo();

    @Test
    public void addAndGetItem() throws Exception {
        Item item = dummyItem(ITEM_ID_1, ITEM_NAME_1);

        itemRepo.add(item);
        Item result = itemRepo.get(ITEM_ID_1);

        assertThat(result, is(equalTo(item)));
    }

    @Test
    public void addTwoDifferentItemsAndGetTheFirstOne() throws Exception {
        Item expected = dummyItem(ITEM_ID_1, ITEM_NAME_1);
        Item notExpected = dummyItem(ITEM_ID_2, ITEM_NAME_2);

        itemRepo.add(expected);
        itemRepo.add(notExpected);
        Item result = itemRepo.get(ITEM_ID_1);

        assertThat(result, is(equalTo(expected)));
        assertThat(result, is(not(equalTo(notExpected))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addSameItemTwice() throws Exception {
        Item item = dummyItem(ITEM_ID_1, ITEM_NAME_1);

        itemRepo.add(item);
        itemRepo.add(item);
    }

    @Test(expected = NoSuchItemException.class)
    public void getNotExistingItemWhenRepoIsEmpty() throws Exception {
        itemRepo.get(ITEM_ID_1);
    }

    @Test(expected = NoSuchItemException.class)
    public void getNotExistingItemWhenRepoIsNotEmpty() throws Exception {
        Item item = dummyItem(ITEM_ID_1, ITEM_NAME_1);

        itemRepo.add(item);
        itemRepo.get(ITEM_ID_2);
    }

    @Test
    public void getExistingItemByName() throws Exception {
        Item item = dummyItem(ITEM_ID_1, ITEM_NAME_1);

        itemRepo.add(item);
        Item result = itemRepo.getByName(ITEM_NAME_1);

        assertThat(result, is(equalTo(item)));
    }

    @Test
    public void getExistingItemByNameIgnoringCase() throws Exception {
        Item item = dummyItem(ITEM_ID_1, ITEM_NAME_1);

        itemRepo.add(item);
        Item result = itemRepo.getByName(ITEM_NAME_1.toUpperCase());

        assertThat(result, is(equalTo(item)));
    }

    @Test(expected = NoSuchItemException.class)
    public void getNotExistingItemByNameFromEmptyRepo() throws Exception {
        itemRepo.getByName(ITEM_NAME_1);
    }

    @Test(expected = NoSuchItemException.class)
    public void getNotExistingItemByName() throws Exception {
        Item item = dummyItem(ITEM_ID_1, ITEM_NAME_1);

        itemRepo.add(item);
        itemRepo.getByName(ITEM_NAME_2);
    }

    @Test
    public void getExistingItemByNameWithAccents() throws Exception {
        Item item = dummyItem(ITEM_ID_1, ITEM_NAME_WITH_ACCENTS);

        itemRepo.add(item);
        Item result = itemRepo.getByName(ITEM_NAME_WITH_ACCENTS);

        assertThat(result, is(equalTo(item)));
    }

    @Test
    public void getExistingItemByNameWithAccentsIgnoringCase() throws Exception {
        Item item = dummyItem(ITEM_ID_1, ITEM_NAME_WITH_ACCENTS);

        itemRepo.add(item);
        Item result = itemRepo.getByName(ITEM_NAME_WITH_ACCENTS.toUpperCase());

        assertThat(result, is(equalTo(item)));
    }

    private Item dummyItem(String id, String name) {
        return Misc.builder()
                .id(id)
                .name(name)
                .build();
    }
}