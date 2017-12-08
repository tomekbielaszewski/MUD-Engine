package org.grizz.game.model.items;

import java.util.List;

public interface Item {
    String getId();
    String getName();
    String getDescription();
    List<? extends ScriptCommandDto> getCommands();
    ItemType getItemType();

    String getBeforeDropScript();

    String getOnDropScript();

    String getBeforeReceiveScript();

    String getOnReceiveScript();
}
