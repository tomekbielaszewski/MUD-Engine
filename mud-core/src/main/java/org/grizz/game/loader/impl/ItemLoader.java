package org.grizz.game.loader.impl;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.model.Script;
import old.org.grizz.game.model.enums.ItemType;
import old.org.grizz.game.model.enums.WeaponType;
import old.org.grizz.game.model.impl.items.*;
import old.org.grizz.game.model.items.CommandScript;
import old.org.grizz.game.model.items.Item;
import old.org.grizz.game.model.repository.Repository;
import old.org.grizz.game.utils.FileUtils;
import org.grizz.game.loader.Loader;
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
        FileUtils.listFilesInFolder(_path)
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
        for (CommandScript commandScript : item.getCommands()) {
            scriptRepo.get(commandScript.getScriptId());
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
        return WeaponEntity.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .itemType(item.itemType)
                .commands(item.commands)
                .weaponType(item.weaponType)
                .minDamage(item.minDamage)
                .maxDamage(item.maxDamage)
                .build();
    }

    private Item transformArmor(UniversalItem item) {
        return ArmorEntity.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .itemType(item.itemType)
                .commands(item.commands)
                .build();
    }

    private Item transformMisc(UniversalItem item) {
        return MiscEntity.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .itemType(item.itemType)
                .commands(item.commands)
                .build();
    }

    private Item transformStatic(UniversalItem item) {
        return StaticEntity.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .pickUpMessage(item.pickUpMessage)
                .itemType(item.itemType)
                .commands(item.commands)
                .build();
    }

    @Value
    private class UniversalItem implements Item {
        String id;
        String name;
        String description;
        String pickUpMessage;
        ItemType itemType;
        List<CommandScriptEntity> commands;
        WeaponType weaponType;
        int minDamage;
        int maxDamage;
    }
}
