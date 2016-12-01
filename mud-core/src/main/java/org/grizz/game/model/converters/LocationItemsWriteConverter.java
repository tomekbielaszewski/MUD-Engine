package org.grizz.game.model.converters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.grizz.game.model.ItemStack;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.items.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationItemsWriteConverter implements Converter<LocationItems, DBObject> {
    @Autowired
    private ItemListToItemStackConverter itemListToItemStackConverter;
    @Autowired
    private ItemStackWriteConverter itemStackConverter;

    @Override
    public DBObject convert(LocationItems source) {
        DBObject dbo = new BasicDBObject();
        if (source.getId() != null) {
            dbo.put("_id", new ObjectId(source.getId()));
        }
        dbo.put("locationId", source.getLocationId());
        dbo.put("staticItems", convert(source.getStaticItems()));
        dbo.put("mobileItems", convert(source.getMobileItems()));
        return dbo;
    }

    private List<DBObject> convert(List<Item> itemList) {
        List<ItemStack> itemStacks = itemListToItemStackConverter.convert(itemList);
        List<DBObject> dbObjects = itemStacks.stream()
                .map(itemStack -> itemStackConverter.convert(itemStack))
                .collect(Collectors.toList());

        return dbObjects;
    }
}
