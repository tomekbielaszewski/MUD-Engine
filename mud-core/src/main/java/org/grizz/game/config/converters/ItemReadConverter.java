package org.grizz.game.config.converters;

import com.mongodb.DBObject;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-10-12.
 */
@Service
public class ItemReadConverter implements Converter<DBObject, Item> {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public Item convert(DBObject source) {
        String id = (String) source.get("_id");
        Item item = itemRepo.get(id);
        return item;
    }
}
