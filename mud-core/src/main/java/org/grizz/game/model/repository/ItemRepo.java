package org.grizz.game.model.repository;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.grizz.game.exception.NoSuchItemException;
import org.grizz.game.model.items.Item;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ItemRepo implements Repository<Item> {
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
            throw new NoSuchItemException("no.such.item", id);
        }
    }

    public Item getByName(String itemName) {
        final String formattedItemName = StringUtils.stripAccents(itemName.trim().toLowerCase())
                .replaceAll("ł", "l")
                .replaceAll("Ł", "L");

        Map.Entry<String, Item> matchingItemEntry = items.entrySet().stream()
                .filter(itemEntry -> {
                    String itemNameFromRepo = itemEntry.getValue().getName();
                    itemNameFromRepo = StringUtils.stripAccents(itemNameFromRepo.trim().toLowerCase())
                            .replaceAll("ł", "l")
                            .replaceAll("Ł", "L");
                    return itemNameFromRepo.equalsIgnoreCase(formattedItemName);
                })
                .findFirst()
                .orElseThrow(() -> new NoSuchItemException("there.is.no.such.item.name", itemName));

        return matchingItemEntry.getValue();
    }
}