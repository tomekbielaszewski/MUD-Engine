package org.grizz.game.model.repository.impl;

import org.grizz.game.model.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-04-21.
 */
@Service
public class ItemRepoImpl implements ItemRepo {
    @Override
    public void add(Item item) {
        System.out.println("Item repo");
    }

    @Override
    public Item get(String id) {
        return null;
    }
}
