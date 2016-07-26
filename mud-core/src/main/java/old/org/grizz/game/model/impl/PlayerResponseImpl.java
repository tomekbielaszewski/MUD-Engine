package old.org.grizz.game.model.impl;

import com.google.common.collect.Lists;
import lombok.Data;
import old.org.grizz.game.model.Location;
import old.org.grizz.game.model.PlayerResponse;
import old.org.grizz.game.model.items.Item;

import java.util.List;

/**
 * Created by Grizz on 2015-04-27.
 */
@Data
public class PlayerResponseImpl implements PlayerResponse {
    private Location currentLocation;
    private List<Item> locationItems = Lists.newArrayList();
    private List<Item> locationStaticItems = Lists.newArrayList();
    private List<Item> equipmentItems = Lists.newArrayList();
    private List<String> possibleExits = Lists.newArrayList();
    private List<String> playerEvents = Lists.newArrayList();
    private List<String> playersOnLocation = Lists.newArrayList();
}
