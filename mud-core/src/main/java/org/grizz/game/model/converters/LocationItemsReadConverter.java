package org.grizz.game.model.converters;

import com.mongodb.DBObject;
import org.grizz.game.model.LocationItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationItemsReadConverter implements Converter<DBObject, LocationItems> {

    @Autowired
    private DBItemPackToItemListConverter itemListConverter;

    @Override
    public LocationItems convert(DBObject source) {
        return LocationItems.builder()
                .id(source.get("_id").toString())
                .locationId(source.get("locationId").toString())
                .staticItems(itemListConverter.convert((List<DBObject>) source.get("staticItems")))
                .mobileItems(itemListConverter.convert((List<DBObject>) source.get("mobileItems")))
                .build();
    }
}
