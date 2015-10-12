package org.grizz.game.model.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;
import org.grizz.game.model.Equipment;
import org.grizz.game.model.PlayerContext;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by tomasz.bielaszewski on 2015-04-23.
 */
@Data
@Builder
@AllArgsConstructor
@Document(collection = "players")
public class PlayerContextImpl implements PlayerContext {
    private String name;

    private int strength;
    private int dexterity;
    private int endurance;
    private int intelligence;
    private int wisdom;
    private int charisma;

    private Equipment equipment;

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
}
