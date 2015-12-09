package org.grizz.game.service.complex.impl;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.PlayerResponse;
import org.grizz.game.model.items.CommandScript;
import org.grizz.game.model.items.Item;
import org.grizz.game.service.complex.PlayerEquipmentInteractionService;
import org.grizz.game.service.complex.ScriptRunnerService;
import org.grizz.game.service.simple.EquipmentService;
import org.grizz.game.service.utils.CommandUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Grizz on 2015-08-22.
 */
@Slf4j
@Component
public class PlayerEquipmentInteractionServiceImpl implements PlayerEquipmentInteractionService {
    @Autowired
    private CommandUtils commandUtils;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ScriptRunnerService scriptRunner;


    @Override
    public boolean canExecuteItemCommand(String command, PlayerContext player) {
        List<Item> itemsInEquipment = equipmentService.getItemsInEquipment(player);
        return getMatchingCommandScript(command, itemsInEquipment) != null;
    }

    @Override
    public void executeItemCommand(String command, PlayerContext player, PlayerResponse response) {
        List<Item> itemsInEquipment = equipmentService.getItemsInEquipment(player);
        CommandScript matchingCommandScript = getMatchingCommandScript(command, itemsInEquipment);
        if (matchingCommandScript != null) {
            scriptRunner.execute(command, matchingCommandScript.getCommand(), matchingCommandScript.getScriptId(), player, response);
        }
    }

    private CommandScript getMatchingCommandScript(String command, List<Item> items) {
        //TODO check for command duplicates on two different items. What to do then?
        CommandScript matchingCommandScript = items.stream()
                .flatMap(item -> item.getCommands().stream())
                .filter(commandScript -> commandUtils.isMatching(command, commandScript.getCommand()))
                .findAny().orElse(null);

        return matchingCommandScript;
    }
}
