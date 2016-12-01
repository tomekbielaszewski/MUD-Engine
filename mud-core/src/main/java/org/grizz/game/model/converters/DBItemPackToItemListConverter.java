package org.grizz.game.model.converters;

import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBItemPackToItemListConverter implements Converter<List<DBObject>, List<Item>> {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public List<Item> convert(List<DBObject> dbObjects) {
        List<Item> items = Lists.newArrayList();
        dbObjects.stream()
                .forEach(dbObject -> {
                    Integer amount = (Integer) dbObject.get("amount");
                    Item item = itemRepo.get((String) dbObject.get("id"));
                    for (int i = 0; i < amount; i++) {
                        items.add(item);
                    }
                });
        return items;
    }
}
