package org.grizz.game.commands.impl;

import org.grizz.game.commands.Command;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.impl.PlayerResponseImpl;
import org.grizz.game.model.items.CommandScript;
import org.grizz.game.model.items.Item;
import org.grizz.game.service.complex.ScriptRunnerService;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.utils.CommandUtils;
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
        return !playerContext.getEquipment().isEmpty() &&
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

        for (Item item : items) {
            for (CommandScript commandScript : item.getCommands()) {
                if (commandMatch(command, commandScript.getCommand())) {
                    return commandScript;
                }
            }
        }
        return null;
    }

    private boolean commandMatch(String command, String commandPattern) {
        return CommandUtils.isMatching(command, commandPattern);
    }

    @Override
    public boolean accept(String command) {
        return false; //Not used - do nothing...
    }
}
