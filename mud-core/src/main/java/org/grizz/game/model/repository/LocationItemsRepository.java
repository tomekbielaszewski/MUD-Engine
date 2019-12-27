package org.grizz.game.model.repository;

import org.grizz.game.model.LocationItems;

public interface LocationItemsRepository /*extends CrudRepository<LocationItems, String>*/ {
    LocationItems findByLocationId(String locationId);

    void update(LocationItems items);

    LocationItems save(LocationItems locationItems);
}
