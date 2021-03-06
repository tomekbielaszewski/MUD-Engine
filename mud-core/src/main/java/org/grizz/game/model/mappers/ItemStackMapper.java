package org.grizz.game.model.mappers;

import org.grizz.game.model.items.Item;
import org.grizz.game.model.repository.ItemRepo;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ItemStackMapper implements RowMapper<List<Item>> {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public List<Item> map(ResultSet rs, StatementContext ctx) throws SQLException {
        int amount = rs.getInt("amount");
        String id = rs.getString("item_id");
        Item item = itemRepo.get(id);

        return IntStream.range(0, amount)
                .mapToObj(i -> item)
                .collect(Collectors.toList());
    }
}
