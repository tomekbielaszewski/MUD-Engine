package org.grizz.game.model.converters;

import com.mongodb.DBObject;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

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
