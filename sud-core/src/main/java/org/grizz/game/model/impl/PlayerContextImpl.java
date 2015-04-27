package org.grizz.game.model.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.PlayerContext;

import java.util.List;
import java.util.Map;

/**
 * Created by tomasz.bielaszewski on 2015-04-23.
 */
@Data
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
    private List<String> events;

    @Override
    public void addAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public boolean containsAttribute(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public void addEvent(String event) {
        events.add(event);
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
                .attributes(this.getAttributes())
                .events(this.events);
    }

    public void setTo(PlayerContextImpl context) {
        this.name = context.name;
        this.strength = context.strength;
        this.dexterity = context.dexterity;
        this.intelligence = context.intelligence;
        this.wisdom = context.wisdom;
        this.charisma = context.charisma;
        this.vitality = context.vitality;
        this.currentLocation = context.currentLocation;
        this.pastLocation = context.pastLocation;
        this.attributes = context.attributes;
        this.events = context.events;
    }
}
