package org.grizz.game.model.mappers;

import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.mapper.ItemStackMapper;
import org.grizz.game.model.repository.ItemRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemStackMapperTest {
    private static final String ID1 = "first id";

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private ItemStackMapper mapper = new ItemStackMapper();

    @Test
    public void convertsEmptyStack() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getString("item_id")).thenReturn(ID1);
        when(resultSet.getInt("amount")).thenReturn(0);

        List<Item> converted = mapper.map(resultSet, null);

        assertThat(converted, is(empty()));
    }

    @Test
    public void convertsSingleItem() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getString("item_id")).thenReturn(ID1);
        when(resultSet.getInt("amount")).thenReturn(1);
        when(itemRepo.get(ID1)).thenReturn(dummyItem(ID1));

        List<Item> converted = mapper.map(resultSet, null);

        assertThat(converted, hasSize(1));
        assertThat(converted, everyItem(hasProperty("id", equalTo(ID1))));
    }

    @Test
    public void convertsManySameItems() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getString("item_id")).thenReturn(ID1);
        when(resultSet.getInt("amount")).thenReturn(10);
        when(itemRepo.get(ID1)).thenReturn(dummyItem(ID1));

        List<Item> converted = mapper.map(resultSet, null);

        assertThat(converted, hasSize(10));
        assertThat(converted, everyItem(hasProperty("id", equalTo(ID1))));
    }

    private Item dummyItem(String id) {
        return Armor.builder()
                .id(id)
                .build();
    }
}
