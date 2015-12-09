package org.grizz.game.loader.impl;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.Script;
import org.grizz.game.model.enums.ItemType;
import org.grizz.game.model.enums.WeaponType;
import org.grizz.game.model.impl.items.*;
import org.grizz.game.model.items.CommandScript;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.Repository;
import org.grizz.game.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by Grizz on 2015-04-17.
 */
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
                            for (CommandScript commandScript : transformedItem.getCommands()) {
                                scriptRepo.get(commandScript.getScriptId());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
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
