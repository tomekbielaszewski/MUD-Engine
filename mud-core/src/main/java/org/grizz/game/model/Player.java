package org.grizz.game.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class Player {
    private String name;
    private String currentLocation;
    private String pastLocation;
    private long lastActivityTimestamp;

    private Stats stats;
    private Equipment equipment;
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

    public boolean hasParameter(String key) {
        return parameters.containsKey(key);
    }
}
