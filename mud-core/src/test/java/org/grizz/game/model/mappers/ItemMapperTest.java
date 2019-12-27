package org.grizz.game.model.mappers;

import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.ResultSet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemMapperTest {
    private static final String ID = "id";

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private ItemMapper mapper = new ItemMapper();

    @Test
    public void shouldGetItemByIdFromResultSet() throws Exception {
        Item expectedItem = dummyItem(ID);
        Mockito.when(itemRepo.get(ID)).thenReturn(expectedItem);

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString("id")).thenReturn(ID);

        Item converted = mapper.map(resultSet, null);

        assertThat(converted.getId(), is(ID));
    }

    private Item dummyItem(String id) {
        return Armor.builder()
                .id(id)
                .build();
    }
}
