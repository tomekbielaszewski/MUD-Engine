package org.grizz.game.model.impl;

import lombok.Value;
import org.grizz.game.model.PlayerContext;

import java.util.Map;

/**
 * Created by tomasz.bielaszewski on 2015-04-23.
 */
@Value
public class PlayerContextImpl implements PlayerContext {
    private String name;

    private int strength;
    private int dexterity;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int vitality;

    private Map<String, Object> attributes;

    @Override
    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public boolean containsAttribute(String key) {
        return attributes.containsKey(key);
    }
}
