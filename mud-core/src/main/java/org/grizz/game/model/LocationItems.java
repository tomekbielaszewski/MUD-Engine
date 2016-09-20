package org.grizz.game.model;

import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.items.Item;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "locationItems")
public class LocationItems {
    @Id
    private String id;

    @Indexed(unique = true)
    private String locationId;
    private List<Item> mobileItems;
    private List<Item> staticItems;
}
