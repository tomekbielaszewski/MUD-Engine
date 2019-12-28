package org.grizz.game.model.db.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EquipmentEntity {
    private String playerName;
    private String head;
    private String torso;
    private String hands;
    private String legs;
    private String feet;
    private String meleeWeapon;
    private String rangedWeapon;
}
