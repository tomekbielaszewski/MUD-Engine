package org.grizz.game.model.mappers;

import org.grizz.game.model.LocationItems;
import org.grizz.game.model.repository.ItemListRepository;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LocationItemsMapper implements RowMapper<LocationItems> {

    @Lazy
    @Autowired
    private ItemListRepository itemListRepository;

    @Override
    public LocationItems map(ResultSet rs, StatementContext ctx) throws SQLException {
        String locationId = rs.getString("location_id");
        String itemListId = rs.getString("item_list_id");
        String staticItemListId = rs.getString("static_item_list_id");

        return LocationItems.builder()
                .locationId(locationId)
                .mobileItems(itemListRepository.get(itemListId))
                .staticItems(itemListRepository.get(staticItemListId))
                .build();
    }
}
