package org.grizz.game.model.repository;

import org.grizz.game.model.items.Item;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface ItemListRepository {
    @SqlQuery("SELECT * FROM item_stacks WHERE id=:id")
    List<Item> get(@Bind String id);
}
