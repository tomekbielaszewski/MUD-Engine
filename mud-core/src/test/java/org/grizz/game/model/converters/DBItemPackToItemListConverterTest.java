package org.grizz.game.model.converters;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.grizz.game.model.items.Armor;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DBItemPackToItemListConverterTest {
    private static final String ID1 = "first id";
    private static final String ID2 = "second id";
    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private DBItemPackToItemListConverter converter = new DBItemPackToItemListConverter();

    @Test
    public void convertsEmptyList() throws Exception {
        List<Item> converted = converter.convert(Lists.newArrayList());

        assertThat(converted, is(empty()));
    }

    @Test
    public void convertsSingleItem() throws Exception {
        List<DBObject> dbObjects = dummyDBOList(
                "amount", 1,
                "id", ID1
        );
        when(itemRepo.get(ID1)).thenReturn(dummyItem(ID1));

        List<Item> converted = converter.convert(dbObjects);

        assertThat(converted, hasSize(1));
        assertThat(converted, everyItem(hasProperty("id", equalTo(ID1))));
    }

    @Test
    public void convertsManySameItems() throws Exception {
        List<DBObject> dbObjects = dummyDBOList(
                "amount", 10,
                "id", ID1
        );
        when(itemRepo.get(ID1)).thenReturn(dummyItem(ID1));

        List<Item> converted = converter.convert(dbObjects);

        assertThat(converted, hasSize(10));
        assertThat(converted, everyItem(hasProperty("id", equalTo(ID1))));
    }

    @Test
    public void convertsTwoDifferentItems() throws Exception {
        List<DBObject> dbObjects = dummyDBOList(
                "amount", 1,
                "id", ID1,
                "amount", 1,
                "id", ID2
        );
        when(itemRepo.get(ID1)).thenReturn(dummyItem(ID1));
        when(itemRepo.get(ID2)).thenReturn(dummyItem(ID2));

        List<Item> converted = converter.convert(dbObjects);

        assertThat(converted, hasSize(2));
        assertThat(converted, hasItem(hasProperty("id", equalTo(ID1))));
        assertThat(converted, hasItem(hasProperty("id", equalTo(ID2))));
    }

    @Test
    public void convertsTwoDifferentSetOfItems() throws Exception {
        List<DBObject> dbObjects = dummyDBOList(
                "amount", 10,
                "id", ID1,
                "amount", 5,
                "id", ID2
        );
        when(itemRepo.get(ID1)).thenReturn(dummyItem(ID1));
        when(itemRepo.get(ID2)).thenReturn(dummyItem(ID2));

        List<Item> converted = converter.convert(dbObjects);

        assertThat(converted, hasSize(15));
        assertThat(converted, hasItems(hasProperty("id", equalTo(ID1))));
        assertThat(converted, hasItems(hasProperty("id", equalTo(ID2))));
    }

    private Item dummyItem(String id) {
        return Armor.builder()
                .id(id)
                .build();
    }

    private List<DBObject> dummyDBOList(Object... args) {
        List<DBObject> dbos = Lists.newArrayList();

        for (int i = 0; i < args.length; i = i + 4) {
            DBObject dbo = new BasicDBObject();

            dbo.put(args[i].toString(), args[i + 1]);
            dbo.put(args[i + 2].toString(), args[i + 3]);

            dbos.add(dbo);
        }

        return dbos;
    }
}