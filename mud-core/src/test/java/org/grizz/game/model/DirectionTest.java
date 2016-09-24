package org.grizz.game.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DirectionTest {
    @Test
    public void fromLocationToSouth() throws Exception {
        Location location = Location.builder().south("southId").build();

        String targetId = Direction.SOUTH.from(location);

        assertThat(targetId, is("southId"));
    }

    @Test
    public void fromLocationToNorth() throws Exception {
        Location location = Location.builder().north("northId").build();

        String targetId = Direction.NORTH.from(location);

        assertThat(targetId, is("northId"));
    }

    @Test
    public void fromLocationToWest() throws Exception {
        Location location = Location.builder().west("westId").build();

        String targetId = Direction.WEST.from(location);

        assertThat(targetId, is("westId"));
    }

    @Test
    public void fromLocationToEast() throws Exception {
        Location location = Location.builder().east("eastId").build();

        String targetId = Direction.EAST.from(location);

        assertThat(targetId, is("eastId"));
    }

    @Test
    public void fromLocationToUp() throws Exception {
        Location location = Location.builder().up("upId").build();

        String targetId = Direction.UP.from(location);

        assertThat(targetId, is("upId"));
    }

    @Test
    public void fromLocationToDown() throws Exception {
        Location location = Location.builder().down("downId").build();

        String targetId = Direction.DOWN.from(location);

        assertThat(targetId, is("downId"));
    }

    @Test
    public void fromLocationToDeniedDirection() throws Exception {
        Location location = Location.builder().build();

        String targetId = Direction.SOUTH.from(location);

        assertThat(targetId, is(nullValue()));
    }

}