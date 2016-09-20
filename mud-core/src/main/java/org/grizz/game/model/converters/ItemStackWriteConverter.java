package org.grizz.game.model.converters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.grizz.game.model.ItemStack;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ItemStackWriteConverter implements Converter<ItemStack, DBObject> {
    @Override
    public DBObject convert(ItemStack source) {
        DBObject dbo = new BasicDBObject();
        dbo.put("id", source.getId());
        dbo.put("name", source.getName());
        dbo.put("amount", source.getAmount());
        return dbo;
    }
}
