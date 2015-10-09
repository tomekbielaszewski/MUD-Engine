package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.items.CommandScript;
import org.grizz.game.model.items.Item;
import org.grizz.game.service.complex.ScriptRunnerService;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
@Component
public class UseEquipmentItemCommand implements Command {
    @Autowired
    private CommandUtils commandUtils;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ScriptRunnerService scriptRunner;

    @Override
    public boolean accept(String command, PlayerContext playerContext) {
        return !equipmentService.getItemsInEquipment(playerContext).isEmpty() &&
                getItemScript(command, playerContext) != null;
    }

    @Override
    public PlayerResponse execute(String command, PlayerContext playerContext) {
        PlayerResponse response = new PlayerResponseImpl();

        CommandScript commandScript = getItemScript(command, playerContext);
        if (commandScript != null) {
            scriptRunner.execute(command, commandScript.getScriptId(), playerContext, response);
        }

        return response;
    }

    private CommandScript getItemScript(String command, PlayerContext playerContext) {
        List<Item> items = equipmentService.getItemsInEquipment(playerContext);

        //TODO stream
        //TODO check for command duplicates on two different items. What to do then?
        for (Item item : items) {
            for (CommandScript commandScript : item.getCommands()) {
                if (commandUtils.isMatching(command, commandScript.getCommand())) {
                    return commandScript;
                }
            }
        }
        return null;
    }

    @Override
    public boolean accept(String command) {
        return false; //Not used - do nothing...
    }
}
