package org.grizz.game.model.impl;

import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.LocationItems;
import org.grizz.game.model.items.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Grizz on 2015-10-12.
 */
@Data
@Builder
@Document(collection = "locationItems")
public class LocationItemsEntity implements LocationItems {
    @Id
    private String id;

    private String locationId;
    private List<Item> mobileItems;
    private List<Item> staticItems;
}
