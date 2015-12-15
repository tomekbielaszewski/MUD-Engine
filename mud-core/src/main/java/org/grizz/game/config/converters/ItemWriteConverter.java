package org.grizz.game.config.converters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.grizz.game.model.items.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-10-12.
 */
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
