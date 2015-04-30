package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.Item;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.repository.ItemRepo;
import org.grizz.game.model.repository.ItemStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-04-30.
 */
@Service
public class ShowEquipmentCommand implements Command {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public boolean accept(String command) {
        return "ekwipunek".equals(command) ||
                "pokaz ekwipunek".equals(command) ||
                "pokaż ekwipunek".equals(command) ||
                "przejrzyj ekwipunek".equals(command);
    }

    @Override
    public void execute(String command, PlayerContext playerContext) {
        List<ItemStack> equipment = playerContext.getEquipment();

        for (ItemStack itemStack : equipment) {
            Item item = itemRepo.get(itemStack.getItemId());

            playerContext.addEvent(formatItemDescription(item, itemStack.getQuantity()));
        }
    }

    private String formatItemDescription(Item item, int quantity) {
        String name = item.getName();

        return String.format("%s x%d", name, quantity);
    }
}
