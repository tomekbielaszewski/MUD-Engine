package org.grizz.game.model;

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

    void addAttribute(String key, Object value);
    Object getAttribute(String key);
    boolean containsAttribute(String key);
}
