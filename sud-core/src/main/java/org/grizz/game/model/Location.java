package org.grizz.game.model;

/**
 * Created by Grizz on 2015-04-21.
 */
public interface Location {
    String getId();

    String getName();

    String getDescription();

    String getEntryScript();

    String getLeavingScript();

    String getSouth();

    String getNorth();

    String getEast();

    String getWest();

    String getUp();

    String getDown();
}
