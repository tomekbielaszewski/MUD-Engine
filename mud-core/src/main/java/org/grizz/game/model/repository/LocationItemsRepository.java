package org.grizz.game.model.repository;

import org.grizz.game.model.Location;
import org.grizz.game.model.LocationItems;
import org.jdbi.v3.sqlobject.customizer.Bind;

public interface LocationItemsRepository {
    LocationItems findByLocationId(@Bind String locationId);

    void update(LocationItems items);

    LocationItems save(Location location);
}
