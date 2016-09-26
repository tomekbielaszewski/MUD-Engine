package org.grizz.game.service.script;

import lombok.Data;
import lombok.experimental.Builder;

@Data
@Builder
public class ScriptBinding {
    private String name;
    private Object object;
}
