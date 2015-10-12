package org.grizz.game.loader.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.loader.Loader;
import org.grizz.game.model.Location;
import org.grizz.game.model.impl.LocationEntity;
import org.grizz.game.model.impl.LocationItemsEntity;
import org.grizz.game.model.repository.LocationItemsRepository;
import org.grizz.game.model.repository.Repository;
import org.grizz.game.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * Created by Grizz on 2015-04-17.
 */
@Slf4j
public class LocationLoader implements Loader {
    private final String _path;

    @Autowired
    private Repository<Location> locationRepo;

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
                    LocationEntity[] locationsArray = null;
                    try {
                        log.info("Reading: {}", path.toString());
                        locationsArray = gson.fromJson(Files.newBufferedReader(path), LocationEntity[].class);
                        for (LocationEntity location : locationsArray) {
                            LocationItemsEntity locationItems = locationItemsRepository.findByLocationId(location.getId());
                            if (locationItems == null) {
                                log.info("Not found location items for location {}. Creating new entity...", location.getName());
                                locationItems = LocationItemsEntity.builder()
                                        .locationId(location.getId())
                                        .mobileItems(Lists.newArrayList())
                                        .staticItems(Lists.newArrayList())
                                        .build();
                                locationItems = locationItemsRepository.insert(locationItems);
                            }
                            location.setItems(locationItems);
                            locationRepo.add(location);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
