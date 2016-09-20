package org.grizz.game.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Builder;

@Data
@Builder
@ToString(exclude = "code")
public class Script {
    private String id;
    private String name;
    private String code;
    private String path;
}
