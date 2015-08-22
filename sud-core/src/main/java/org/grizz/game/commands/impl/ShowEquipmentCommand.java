package org.grizz.game.commands.impl;

import com.google.common.collect.Lists;
import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.items.Item;
import org.grizz.game.service.simple.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-04-30.
 */
@Service
public class ShowEquipmentCommand implements Command {
    @Autowired
    private Environment env;
    @Autowired
    private EquipmentService equipmentService;

    @Override
    public boolean accept(String command) {
        String commands = env.getProperty(getClass().getCanonicalName());
        return Lists.newArrayList(commands.split(";")).contains(command);
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();

        List<Item> itemsInEquipment = equipmentService.getItemsInEquipment(playerContext);
        response.getEquipmentItems().addAll(itemsInEquipment);

        return response;
    }
}
