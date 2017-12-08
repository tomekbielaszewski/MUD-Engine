package org.grizz.game.model.converters;

import com.google.common.collect.Maps;
import org.grizz.game.model.items.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemListToCountedItemsConverter implements Converter<List<Item>, Map<Item, Integer>> {

    @Override
    public Map<Item, Integer> convert(List<Item> source) {
        HashMap<Item, Integer> countedItems = Maps.newHashMap();

        source.forEach(item -> {
            if (!countedItems.containsKey(item)) {
                countedItems.put(item, Collections.frequency(source, item));
            }
        });

        return countedItems;
    }
}
