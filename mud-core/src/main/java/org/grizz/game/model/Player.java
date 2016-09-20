package org.grizz.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "players")
public class Player {
    @Id
    private String id;

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

    public void addParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public void removeParameter(String key) {
        parameters.remove(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }
}
