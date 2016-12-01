package org.grizz.game.command.provider;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.command.Command;
import org.grizz.game.command.parsers.system.ScriptCommand;
import org.grizz.game.model.Player;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ScriptRepo;
import org.grizz.game.service.script.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EquipmentCommandsProvider implements CommandsProvider {
    @Autowired
    private Environment environment;
    @Autowired
    private ScriptRunner scriptRunner;
    @Autowired
    private ScriptRepo scriptRepo;

    @Override
    public List<Command> provide(Player player) {
        List<Command> commands = getItems(player).stream()
                .map(Item::getCommands)
                .flatMap(Collection::stream)
                .map(c -> new ScriptCommand(c, scriptRepo, scriptRunner, environment))
                .collect(Collectors.toList());

        log.info("Collected {} equipment commands", commands.size());

        return commands;
    }

    private List<Item> getItems(Player player) {
        List<Item> items = Lists.newArrayList();

        items.addAll(player.getEquipment().getBackpack());

        addIfNotNull(player.getEquipment().getHeadItem(), items);
        addIfNotNull(player.getEquipment().getTorsoItem(), items);
        addIfNotNull(player.getEquipment().getHandsItem(), items);
        addIfNotNull(player.getEquipment().getLegsItem(), items);
        addIfNotNull(player.getEquipment().getFeetItem(), items);

        addIfNotNull(player.getEquipment().getMeleeWeapon(), items);
        addIfNotNull(player.getEquipment().getRangeWeapon(), items);

        return items;
    }

    private void addIfNotNull(Item item, List<Item> items) {
        if(item != null) items.add(item);
    }
}
