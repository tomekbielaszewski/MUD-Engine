package org.grizz.game.model;

/**
 * Created by tomasz.bielaszewski on 2015-04-23.
 */
public interface PlayerContext {
    String getName();

    int getStrength();
    int getDexterity();
    int getEndurance();
    int getIntelligence();
    int getWisdom();
    int getCharisma();

    Equipment getEquipment();

    String getCurrentLocation();
    String getPastLocation();

    void addParameter(String key, Object value);

    Object getParameter(String key);

    void removeParameter(String key);

    boolean containsParameter(String key);
}
