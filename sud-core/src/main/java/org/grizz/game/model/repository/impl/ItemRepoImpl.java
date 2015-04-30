package org.grizz.game.model.repository.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Grizz on 2015-04-21.
 */
@Slf4j
@Service
public class ItemRepoImpl implements ItemRepo {
    private Map<String, Item> items = Maps.newHashMap();

    @Override
    public void add(Item item) {
        log.info("ItemRepo.add({})", item);

        if (items.containsKey(item.getId())) {
            throw new IllegalArgumentException("Item ID[" + item.getId() + "] is duplicated");
        }

        items.put(item.getId(), item);
    }

    @Override
    public Item get(String id) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new IllegalArgumentException("No such item: " + id);
        }
    }
}
