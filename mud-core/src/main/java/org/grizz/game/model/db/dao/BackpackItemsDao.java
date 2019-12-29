package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.BackpackItemsEntity;
import org.grizz.game.model.db.mappers.BackpackItemsEntityMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

public interface BackpackItemsDao {
    @UseRowMapper(BackpackItemsEntityMapper.class)
    @SqlQuery("SELECT player_name, item_id, item_name, amount " +
            "FROM backpack_items " +
            "WHERE player_name = :name;")
    List<BackpackItemsEntity> getByName(@Bind String name);

    @SqlBatch("INSERT INTO backpack_items " +
            "VALUES (:b.playerName, :b.itemId, :b.itemName, :b.amount) " +
            "ON CONFLICT (player_name, item_id) DO UPDATE SET amount = excluded.amount;")
    void insert(@BindBean("b") List<BackpackItemsEntity> toBackpackItemsEntities);

    @SqlUpdate("DELETE FROM backpack_items " +
            "WHERE player_name=:name;")
    void removeAll(@Bind String name);
}
