package org.grizz.game.model.db.dao;

import org.grizz.game.model.db.entities.BackpackItemsEntity;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface BackpackItemsDao {
    @SqlQuery("SELECT backpack.item_name, backpack.amount, backpack.player_name, backpack.item_id " +
            "FROM players, " +
            "     backpack_items as backpack " +
            "WHERE players.name = backpack.player_name " +
            "  AND players.name = :playerName;")
    List<BackpackItemsEntity> getByName(@Bind String playerName);

    @SqlQuery("INSERT INTO backpack_items " +
            "VALUES (:b.playerName, :b.itemId, :b.itemName, :b.amount) " +
            "ON CONFLICT (player_name, item_id) DO UPDATE SET amount = excluded.amount;")
    List<BackpackItemsEntity> upsert(@BindBean("b") BackpackItemsEntity backpackItemsEntity);

    void upsert(List<BackpackItemsEntity> toBackpackItemsEntities);
}
