package org.grizz.game.loader.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.Item;
import org.grizz.game.model.impl.ItemEntity;
import org.grizz.game.model.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Grizz on 2015-04-17.
 */
@Slf4j
public class ItemLoader implements Loader {
    private final String _path;

    @Autowired
    private Repository<Item> itemRepo;

    public ItemLoader(String path) {
        this._path = path;
    }

    @Override
    public void load() {
        String path = "";

        List<ItemEntity> items = readItems(path);
        storeItems(items);
    }

    private List<ItemEntity> readItems(String path) {
        return Lists.newArrayList();
    }

    private void storeItems(List<ItemEntity> items) {
        for (ItemEntity item : items) {
            itemRepo.add(item);
        }
    }
}
