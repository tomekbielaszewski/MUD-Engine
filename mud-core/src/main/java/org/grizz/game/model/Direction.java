package org.grizz.game.model;

import java.util.function.Function;

public enum Direction {
    SOUTH(Location::getSouth),
    NORTH(Location::getNorth),
    WEST(Location::getWest),
    EAST(Location::getEast),
    UP(Location::getUp),
    DOWN(Location::getDown);

    private final Function<Location, String> directionResolver;

    Direction(Function<Location, String> directionResolver) {
        this.directionResolver = directionResolver;
    }

    public String from(Location location) {
        return directionResolver.apply(location);
    }
}
