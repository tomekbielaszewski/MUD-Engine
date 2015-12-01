package org.grizz.game.model.repository.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.Location;
import org.grizz.game.model.repository.LocationRepo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Grizz on 2015-04-21.
 */
@Slf4j
@Service
public class LocationRepoImpl implements LocationRepo {
    private Map<String, Location> locations = Maps.newHashMap();

    @Override
    public void add(Location location) {
        log.info("LocationRepo.add({})", location);

        if(locations.containsKey(location.getId())) {
            throw new IllegalArgumentException("Location ID["+location.getId()+"] is duplicated");
        }

        locations.put(location.getId(), location);
    }

    @Override
    public Location get(String id) {
        if(locations.containsKey(id)) {
            return locations.get(id);
        } else { //TODO: custom exception
            throw new IllegalArgumentException("No such location: " + id);
        }
    }
}
