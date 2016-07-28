package old.org.grizz.game.model.impl;

import lombok.Data;
import lombok.experimental.Builder;
import old.org.grizz.game.model.LocationItems;
import old.org.grizz.game.model.items.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Indexed(unique = true)
    private String locationId;
    private List<Item> mobileItems;
    private List<Item> staticItems;
}