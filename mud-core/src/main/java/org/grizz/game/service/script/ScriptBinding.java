package org.grizz.game.service.script;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScriptBinding {
    private String name;
    private Object object;
}
