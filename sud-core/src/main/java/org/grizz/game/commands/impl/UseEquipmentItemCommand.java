package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemScript;
import org.grizz.game.service.EquipmentService;
import org.grizz.game.service.ScriptRunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
@Component
public class UseEquipmentItemCommand implements Command {
    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ScriptRunnerService scriptRunner;

    @Override
    public boolean accept(String command, PlayerContext playerContext) {
        return !playerContext.getEquipment().isEmpty();
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();
        List<Item> items = equipmentService.getItemsInEquipment(playerContext);

        //FIXME: Try to change it with stream
        for (Item item : items) {
            for (ItemScript itemScript : item.getCommands()) {
                if (commandMatch(command, itemScript.getCommand())) {
                    scriptRunner.execute(command, itemScript.getScriptId(), playerContext, response);
                    return response;
                }
            }
        }

        return response;
    }

    private boolean commandMatch(String command, String commandDefinition) {
        return command.equals(commandDefinition);
    }

    @Override
    public boolean accept(String command) {
        return false; //Not used - do nothing...
    }
}
