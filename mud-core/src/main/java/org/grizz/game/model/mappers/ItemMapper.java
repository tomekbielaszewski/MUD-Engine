package org.grizz.game.model.mappers;

import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ItemMapper implements RowMapper<Item> {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public Item map(ResultSet rs, StatementContext ctx) throws SQLException {
        String id = rs.getString("id");
        return itemRepo.get(id);
    }
}
