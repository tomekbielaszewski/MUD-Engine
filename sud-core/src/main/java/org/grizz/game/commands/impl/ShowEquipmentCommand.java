package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.items.Item;
import org.grizz.game.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-04-30.
 */
@Service
public class ShowEquipmentCommand implements Command {
    @Autowired
    private EquipmentService equipmentService;

    @Override
    public boolean accept(String command) {
        return "ekwipunek".equals(command) ||
                "pokaz ekwipunek".equals(command) ||
                "przejrzyj ekwipunek".equals(command);
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();

        List<Item> itemsInEquipment = equipmentService.getItemsInEquipment(playerContext);
        response.getEquipmentItems().addAll(itemsInEquipment);

        return response;
    }
}