package org.grizz.game.loader.impl;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.Script;
import org.grizz.game.model.items.*;
import org.grizz.game.model.repository.Repository;
import org.grizz.game.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

@Slf4j
public class ItemLoader implements Loader {
    private final String _path;

    @Autowired
    private Repository<Item> itemRepo;
    @Autowired
    private Repository<Script> scriptRepo;
    @Autowired
    private FileUtils fileUtils;

    public ItemLoader(String path) {
        this._path = path;
    }

    @Override
    @SneakyThrows
    public void load() {
        readItems(_path);
    }

    private void readItems(String _path) throws IOException, URISyntaxException {
        Gson gson = new Gson();
        fileUtils.listFilesInFolder(_path)
                .forEach(path -> {
                    UniversalItem[] itemsArray = null;
                    try {
                        log.info("Reading: {}", path.toString());
                        itemsArray = gson.fromJson(Files.newBufferedReader(path), UniversalItem[].class);
                        for (UniversalItem item : itemsArray) {
                            Item transformedItem = transformItem(item);
                            itemRepo.add(transformedItem);
                            checkItemScripts(transformedItem);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void checkItemScripts(Item item) {
        for (ScriptCommandDto scriptCommandDto : item.getCommands()) {
            scriptRepo.get(scriptCommandDto.getScriptId());
        }
        checkScript(item.getOnDropScript());
        checkScript(item.getBeforeDropScript());
        checkScript(item.getOnReceiveScript());
        checkScript(item.getBeforeReceiveScript());
    }

    private void checkScript(String scriptId) {
        if (!StringUtils.isEmpty(scriptId)) {
            scriptRepo.get(scriptId);
        }
    }

    private Item transformItem(UniversalItem item) {
        switch (item.itemType) {
            case WEAPON:
                return transformWeapon(item);
            case ARMOR:
                return transformArmor(item);
            case MISC:
                return transformMisc(item);
            case STATIC:
                return transformStatic(item);
        }

        throw new NotImplementedException();
    }

    private Item transformWeapon(UniversalItem item) {
        return Weapon.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .commands(item.commands)
                .weaponType(item.weaponType)
                .minDamage(item.minDamage)
                .maxDamage(item.maxDamage)
                .onDropScript(item.onDropScript)
                .beforeDropScript(item.beforeDropScript)
                .onReceiveScript(item.onReceiveScript)
                .beforeReceiveScript(item.beforeReceiveScript)
                .build();
    }

    private Item transformArmor(UniversalItem item) {
        return Armor.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .commands(item.commands)
                .onDropScript(item.onDropScript)
                .beforeDropScript(item.beforeDropScript)
                .onReceiveScript(item.onReceiveScript)
                .beforeReceiveScript(item.beforeReceiveScript)
                .build();
    }

    private Item transformMisc(UniversalItem item) {
        return Misc.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .commands(item.commands)
                .onDropScript(item.onDropScript)
                .beforeDropScript(item.beforeDropScript)
                .onReceiveScript(item.onReceiveScript)
                .beforeReceiveScript(item.beforeReceiveScript)
                .build();
    }

    private Item transformStatic(UniversalItem item) {
        return Static.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .pickUpMessage(item.pickUpMessage)
                .commands(item.commands)
                .onDropScript(item.onDropScript)
                .beforeDropScript(item.beforeDropScript)
                .onReceiveScript(item.onReceiveScript)
                .beforeReceiveScript(item.beforeReceiveScript)
                .build();
    }

    @Value
    private class UniversalItem implements Item {
        String id;
        String name;
        String description;
        String pickUpMessage;
        ItemType itemType;
        List<ScriptCommandDto> commands;
        WeaponType weaponType;
        int minDamage;
        int maxDamage;

        String onDropScript;
        String beforeDropScript;
        String onReceiveScript;
        String beforeReceiveScript;
    }
}
