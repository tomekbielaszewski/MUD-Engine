package org.grizz.game.model;

import org.grizz.game.model.items.ItemStack;

import java.util.List;

/**
 * Created by Grizz on 2015-04-21.
 */
public interface Location {
    String getId();

    String getName();

    String getDescription();

    String getSouth();

    String getNorth();

    String getEast();

    String getWest();

    String getUp();

    String getDown();

    List<ItemStack> getItems();

    List<ItemStack> getStaticItems();

    String getBeforeEnter();

    String getOnEnter();

    String getOnShow();

    String getBeforeLeave();

    String getOnLeave();
}
