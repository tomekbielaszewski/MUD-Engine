package old.org.grizz.game.model.items;

import old.org.grizz.game.model.enums.ItemType;

import java.util.List;

/**
 * Created by Grizz on 2015-04-21.
 */
public interface Item {
    String getId();

    String getName();

    String getDescription();

    List<? extends CommandScript> getCommands();

    ItemType getItemType();
}
