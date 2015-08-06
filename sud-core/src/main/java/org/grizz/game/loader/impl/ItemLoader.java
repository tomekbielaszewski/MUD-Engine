package org.grizz.game.loader.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.enums.ItemType;
import org.grizz.game.model.impl.items.ArmorEntity;
import org.grizz.game.model.impl.items.ItemScriptEntity;
import org.grizz.game.model.impl.items.MiscEntity;
import org.grizz.game.model.impl.items.WeaponEntity;
import org.grizz.game.model.items.Item;
import org.grizz.game.model.items.ItemScript;
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

    public ItemLoader(String path) {
        this._path = path;
    }

    @Override
    @SneakyThrows
    public void load() {
        readItems(_path);
    }

    private void readItems(String _path) throws IOException, URISyntaxException {
        Gson gson = new GsonBuilder().registerTypeAdapter(ItemScript.class, (InstanceCreator<ItemScript>) type -> ItemScriptEntity.builder().build()).create();
        FileUtils.listFilesInFolder(_path)
                .forEach(path -> {
                    UniversalItem[] itemsArray = null;
                    try {
                        log.info("Reading: {}", path.toString());
                        itemsArray = gson.fromJson(Files.newBufferedReader(path), UniversalItem[].class);
                        for (UniversalItem item : itemsArray) {
                            itemRepo.add(transformItem(item));
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
        }

        throw new NotImplementedException();
    }

    private Item transformWeapon(UniversalItem item) {
        return WeaponEntity.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .itemType(item.itemType)
                .build();
    }

    private Item transformArmor(UniversalItem item) {
        return ArmorEntity.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .itemType(item.itemType)
                .build();
    }

    private Item transformMisc(UniversalItem item) {
        return MiscEntity.builder()
                .id(item.id)
                .name(item.name)
                .description(item.description)
                .itemType(item.itemType)
                .build();
    }

    @Value
    private class UniversalItem implements Item {
        String id;
        String name;
        String description;
        ItemType itemType;
        private List<ItemScript> commands;
    }
}
