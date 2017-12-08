package org.grizz.game.model.items;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScriptCommandDto {
    private String command;
    private String scriptId;
}
