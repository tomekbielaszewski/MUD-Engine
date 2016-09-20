package org.grizz.game.model.converters;

import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationItemsReadConverter implements Converter<DBObject, LocationItems> {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public LocationItems convert(DBObject source) {
        return LocationItems.builder()
                .id(source.get("_id").toString())
                .locationId((String) source.get("locationId"))
                .staticItems(convert((List<DBObject>) source.get("staticItems")))
                .mobileItems(convert((List<DBObject>) source.get("mobileItems")))
                .build();
    }

    private List<Item> convert(List<DBObject> mobileItemsAsDBObject) {
        List<Item> items = Lists.newArrayList();
        mobileItemsAsDBObject.stream()
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
