package org.grizz.game.config.converters;

import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import org.grizz.game.model.impl.LocationItemsEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Grizz on 2015-10-12.
 */
@Service
public class LocationItemsReadConverter implements Converter<DBObject, LocationItemsEntity> {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public LocationItemsEntity convert(DBObject source) {
        return LocationItemsEntity.builder()
                .id(source.get("_id").toString())
                .locationId((String) source.get("locationId"))
                .staticItems((List<Item>) source.get("staticItems"))
                .mobileItems(convert((List<DBObject>) source.get("mobileItems")))
                .build();
    }

    private List<Item> convert(List<DBObject> mobileItemsAsDBObject) {
        List<Item> items = Lists.newArrayList();
        mobileItemsAsDBObject.stream()
                .forEach(dbObject -> {
                    Integer amount = (Integer) dbObject.get("amount");
                    for (int i = 0; i < amount; i++) {
                        Item item = itemRepo.get((String) dbObject.get("id"));
                        items.add(item);
                    }
                });
        return items;
    }
}
