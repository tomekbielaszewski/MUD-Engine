package org.grizz.game.model.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.grizz.game.model.Location;
import org.grizz.game.model.repository.LocationRepo;
import org.springframework.stereotype.Service;

/**
 * Created by Grizz on 2015-04-21.
 */
@Service
@Slf4j
public class LocationRepoImpl implements LocationRepo {
    @Override
    public void add(Location location) {
        log.info("LocationRepo.add({})", location);
    }

    @Override
    public Location get(String id) {
        return null;
    }
}
