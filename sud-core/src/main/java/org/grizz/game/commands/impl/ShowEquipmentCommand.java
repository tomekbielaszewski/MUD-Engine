package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.Item;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.service.simple.EventService;
import org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-04-30.
 */
@Service
public class ShowEquipmentCommand implements Command {
    @Autowired
    private CommandUtils commandUtils;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EventService eventService;

    @Override
    public boolean accept(String command) {
        return commandUtils.isAnyMatching(command, getClass().getCanonicalName());
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext, PlayerResponse response) {
        List<Item> itemsInEquipment = equipmentService.getItemsInEquipment(playerContext);
        response.getEquipmentItems().addAll(itemsInEquipment);

        String equipmentTitle = "";
        if (itemsInEquipment.isEmpty()) {
            equipmentTitle = eventService.getEvent("equipment.empty");
        } else {
            equipmentTitle = eventService.getEvent("equipment.title");
        }
        response.getPlayerEvents().add(equipmentTitle);

        return response;
    }
}
