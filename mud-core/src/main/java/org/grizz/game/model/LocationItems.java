package org.grizz.game.model;

import lombok.Builder;
import lombok.Data;
import org.grizz.game.model.items.Item;

import java.util.List;

@Data
@Builder
//@Document(collection = "locationItems")
public class LocationItems {
    //    @Id
    private String id;

    //    @Indexed(unique = true)
    private String locationId;
    private List<Item> mobileItems;
    private List<Item> staticItems;
}
