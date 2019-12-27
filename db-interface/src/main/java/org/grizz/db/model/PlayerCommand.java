package org.grizz.db.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@Document(collection = "commandQueue")
public class PlayerCommand {
    private String id;
    private String player;
    private String command;
    private boolean processed;
    private long timestamp;
}
