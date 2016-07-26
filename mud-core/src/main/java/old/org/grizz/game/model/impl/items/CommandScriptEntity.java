package old.org.grizz.game.model.impl.items;

import lombok.Data;
import lombok.experimental.Builder;
import old.org.grizz.game.model.items.CommandScript;

/**
 * Created by tomasz.bielaszewski on 2015-08-06.
 */
@Data
@Builder
public class CommandScriptEntity implements CommandScript {
    private String command;
    private String scriptId;
}
