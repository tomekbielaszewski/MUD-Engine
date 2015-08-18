package org.grizz.game.model.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.PlayerContext;
import org.grizz.game.model.items.ItemStack;

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

    private List<ItemStack> equipment;

    private String currentLocation;
    private String pastLocation;

    private Map<String, Object> parameters;

    @Override
    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    @Override
    public Object getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public void removeParameter(String key) {
        parameters.remove(key);
    }

    @Override
    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
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
                .equipment(this.getEquipment())
                .currentLocation(this.getCurrentLocation())
                .pastLocation(this.getPastLocation())
                .parameters(this.getParameters());
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
        this.parameters = context.parameters;
        this.equipment = context.equipment;
    }
}
