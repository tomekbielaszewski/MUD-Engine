package org.grizz.game.service.impl;

import com.google.common.collect.Lists;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemStack;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-05-08.
 */
@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public void show(PlayerContext context) {
        List<ItemStack> equipment = context.getEquipment();

        for (ItemStack itemStack : equipment) {
            Item item = itemRepo.get(itemStack.getItemId());

            context.addEvent(formatItemDescription(item, itemStack.getQuantity()));
        }
    }

    @Override
    public List<Item> getItemsInEquipment(PlayerContext context) {
        List<ItemStack> equipmentAsItemStack = context.getEquipment();
        List<Item> equipmentAsItems = Lists.newArrayList();

        for (ItemStack itemStack : equipmentAsItemStack) {
            Item item = itemRepo.get(itemStack.getItemId());
            equipmentAsItems.add(item);
        }

        return equipmentAsItems;
    }

    private String formatItemDescription(Item item, int quantity) {
        String name = item.getName();

        return String.format("%s x%d", name, quantity);
    }
}
