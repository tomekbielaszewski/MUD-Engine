package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.LocationEntity;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface LocationsDao {
    @SqlQuery("SELECT name " +
            "FROM locations " +
            "WHERE location_id = :locationId;")
    Optional<String> checkExistence(@Bind String locationId);

    @SqlUpdate("INSERT INTO locations " +
            "VALUES (:l.locationId, :l.name);")
    void insert(@BindBean("l") LocationEntity locationEntity);
}
