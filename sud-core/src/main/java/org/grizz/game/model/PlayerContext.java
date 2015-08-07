package org.grizz.game.model;

import org.grizz.game.model.items.ItemStack;

import java.util.List;

/**
 * Created by tomasz.bielaszewski on 2015-04-23.
 */
public interface PlayerContext {
    String getName();

    int getStrength();
    int getDexterity();
    int getIntelligence();
    int getWisdom();
    int getCharisma();
    int getVitality();

    List<ItemStack> getEquipment();

    String getCurrentLocation();
    String getPastLocation();

    void addParameter(String key, Object value);

    Object getParameter(String key);

    void removeParameter(String key);

    boolean containsParameter(String key);
}
