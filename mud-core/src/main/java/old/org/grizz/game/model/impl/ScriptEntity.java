package old.org.grizz.game.model.impl;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Builder;
import old.org.grizz.game.model.Script;

/**
 * Created by Grizz on 2015-04-21.
 */
@Data
@Builder
@ToString(exclude = "code")
public class ScriptEntity implements Script {
    private String id;
    private String name;
    private String code;
    private String path;
}
