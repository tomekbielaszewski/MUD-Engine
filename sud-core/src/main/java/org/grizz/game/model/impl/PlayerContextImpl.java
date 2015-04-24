package org.grizz.game.model.impl;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Builder;
import org.grizz.game.model.PlayerContext;

import java.util.Map;

/**
 * Created by tomasz.bielaszewski on 2015-04-23.
 */
@Value
@Builder
@AllArgsConstructor
public class PlayerContextImpl implements PlayerContext {
    private String name;

    private int strength;
    private int dexterity;
    private int intelligence;
    private int wisdom;
    private int charisma;
    private int vitality;

    private String currentLocation;
    private String pastLocation;

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

    public PlayerContextImpl.PlayerContextImplBuilder copy() {
        return PlayerContextImpl.builder()
                .name(this.getName())
                .strength(this.getStrength())
                .dexterity(this.getDexterity())
                .intelligence(this.getIntelligence())
                .wisdom(this.getWisdom())
                .charisma(this.getCharisma())
                .vitality(this.getVitality())
                .currentLocation(this.getCurrentLocation())
                .pastLocation(this.getPastLocation())
                .attributes(this.getAttributes());
    }
}
