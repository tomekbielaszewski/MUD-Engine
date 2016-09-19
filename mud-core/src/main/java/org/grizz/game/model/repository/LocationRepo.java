package org.grizz.game.model.repository;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import old.org.grizz.game.exception.NoSuchLocationException;
import old.org.grizz.game.model.Location;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class LocationRepo implements Repository<Location> {
    private Map<String, Location> locations = Maps.newHashMap();

    @Override
    public void add(Location location) {
        log.info("LocationRepo.add({})", location);

        if (locations.containsKey(location.getId())) {
            throw new IllegalArgumentException("Location ID[" + location.getId() + "] is duplicated");
        }

        locations.put(location.getId(), location);
    }

    @Override
    public Location get(String id) {
        if (locations.containsKey(id)) {
            return locations.get(id);
        } else {
            throw new NoSuchLocationException("no.such.location", id);
        }
    }
}
