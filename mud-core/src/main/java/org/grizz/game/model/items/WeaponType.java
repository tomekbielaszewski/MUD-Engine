package org.grizz.game.model.items;

import lombok.Getter;

public enum WeaponType {
    SWORD1H(false, true),
    SWORD2H(false, false),
    DAGGER(false, true),
    BOW(true, false),
    CROSSBOW(true, false),
    AXE1H(false, true),
    AXE2H(false, false),
    HAMMER(false, false),
    POLEARM(false, false),
    STAFF(false, false),
    THROWN(true, true);

    WeaponType(boolean range, boolean oneHanded) {
        this.range = range;
        this.oneHanded = oneHanded;
    }

    @Getter
    private boolean range;
    @Getter
    private boolean oneHanded;
}
