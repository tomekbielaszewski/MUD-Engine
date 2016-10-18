package org.grizz.game.service;

import com.google.common.collect.Lists;
import org.grizz.game.exception.InvalidAmountException;
import org.grizz.game.model.Player;
import org.grizz.game.model.items.Item;

import java.util.List;

public class EquipmentService {
    public List<Item> takeOutItems(String itemName, int amount, Player player) {
        if (amount <= 0) {
            throw new InvalidAmountException("cant.take.out.none.items");
        }

        List<Item> items = Lists.newArrayList();



        return items;
    }
}
