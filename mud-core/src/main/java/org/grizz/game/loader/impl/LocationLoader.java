package org.grizz.game.loader.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.Location;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.Script;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.grizz.game.model.repository.Repository;
import org.grizz.game.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

@Slf4j
public class LocationLoader implements Loader {
    private final String _path;

    @Autowired
    private Repository<Location> locationRepo;
    @Autowired
    private Repository<Script> scriptRepo;

    @Autowired
    private LocationItemsRepository locationItemsRepository;

    public LocationLoader(String path) {
        this._path = path;
    }

    @Override
    @SneakyThrows
    public void load() {
        readLocations(_path);
    }

    private void readLocations(String _path) throws IOException, URISyntaxException {
        Gson gson = new Gson();
        FileUtils.listFilesInFolder(_path)
                .forEach(path -> {
                    Location[] locationsArray = null;
                    try {
                        log.info("Reading: {}", path.toString());
                        locationsArray = gson.fromJson(Files.newBufferedReader(path), Location[].class);
                        for (Location location : locationsArray) {
                            loadLocationItems(location);
                            failFastOnScriptMissing(location);
                            locationRepo.add(location);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void loadLocationItems(Location location) {
        LocationItems locationItems = locationItemsRepository.findByLocationId(location.getId());
        if (locationItems == null) {
            locationItems = initializeDefaultLocationItems(location);
        }
        location.setItems(locationItems);
    }

    private LocationItems initializeDefaultLocationItems(Location location) {
        LocationItems locationItems;
        log.info("Not found location items for location {}. Creating new entity...", location.getName());
        locationItems = LocationItems.builder()
                .locationId(location.getId())
                .mobileItems(Lists.newArrayList())
                .staticItems(Lists.newArrayList())
                .build();
        locationItems = locationItemsRepository.insert(locationItems);
        return locationItems;
    }

    private void failFastOnScriptMissing(Location location) {
        checkScript(location.getBeforeEnter());
        checkScript(location.getOnEnter());
        checkScript(location.getOnShow());
        checkScript(location.getBeforeLeave());
        checkScript(location.getOnLeave());
    }

    private void checkScript(String scriptId) {
        if (!StringUtils.isEmpty(scriptId)) {
            scriptRepo.get(scriptId);
        }
    }
}
