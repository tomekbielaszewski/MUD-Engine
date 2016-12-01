package org.grizz.game.model.converters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.grizz.game.model.items.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class ItemWriteConverter implements Converter<Item, DBObject> {
    @Override
    public DBObject convert(Item source) {
        DBObject dbo = new BasicDBObject();
        dbo.put("_id", source.getId());
        dbo.put("name", source.getName());
        return dbo;
    }
}
