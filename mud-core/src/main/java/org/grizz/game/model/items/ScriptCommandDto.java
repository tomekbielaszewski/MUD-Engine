package org.grizz.game.model.items;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class ScriptCommandDto {
    private String command;
    private String scriptId;
}
