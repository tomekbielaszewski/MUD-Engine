package org.grizz.game.model;

import org.grizz.game.model.repository.ItemStack;

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

    void addAttribute(String key, Object value);
    Object getAttribute(String key);
    void removeAttribute(String key);
    boolean containsAttribute(String key);

    //Dodawany bedzie tu kontekstowy output dla niektorych akcji typu:
    //"podniosles miecz"
    //"Te drzwi sa zamkniete..."
    void addEvent(String event);

    List<String> getEvents();
}
