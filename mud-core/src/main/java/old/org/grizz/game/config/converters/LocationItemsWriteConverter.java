package old.org.grizz.game.config.converters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import old.org.grizz.game.model.impl.ItemStack;
import old.org.grizz.game.model.impl.LocationItemsEntity;
import old.org.grizz.game.model.items.Item;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Grizz on 2015-11-27.
 */
@Service
public class LocationItemsWriteConverter implements Converter<LocationItemsEntity, DBObject> {
    @Autowired
    private ItemListToItemStackConverter itemListToItemStackConverter;
    @Autowired
    private ItemStackWriteConverter itemStackConverter;

    @Override
    public DBObject convert(LocationItemsEntity source) {
        DBObject dbo = new BasicDBObject();
        if(source.getId() != null) {
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
